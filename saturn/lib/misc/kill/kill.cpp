// kill.cpp : Defines the entry point for the console application.
// Copyright (c) 2005 MySQL AB. Authors: Eric Herman and Kendrick Shaw.
#include "stdafx.h"
#include <stdarg.h>
#include <winbase.h>

struct Parsed_params {
	enum Operation { TEST, USAGE, EXISTS, KILL };
	Operation operation;
	DWORD pid;
};

typedef bool (*process_test_func)(HANDLE h_process);
int main(int argc, const char** argv);

// returns false if process does not exist
bool apply_to_process(DWORD process_id, process_test_func test_func)
{
	HANDLE h_process = OpenProcess(PROCESS_TERMINATE, FALSE, process_id);

	if (h_process == NULL)
		return false;
	
	bool return_val= test_func(h_process);

	CloseHandle(h_process);
	
	return return_val;
}

bool known_process_inner(HANDLE h_process)
{
	// already returns false if process doesn't exist
	return true;
}

bool known_process(DWORD process_id)
{
	return apply_to_process(process_id, known_process_inner);
}

bool kill_process_inner(HANDLE h_process)
{
	return (TerminateProcess(h_process, 0) != FALSE);
}

bool kill_process(DWORD process_id)
{
	static int ret_val; 

	ret_val = apply_to_process(process_id, kill_process_inner);
	Sleep(2000);
	return ret_val;

	//if (apply_to_process(process_id, kill_process_inner))
	//{
	//	for(int i = 0; i < 600 && known_process(process_id); i++)
	//	{
	//		Sleep(100);
	//	}
	//	return !known_process(process_id);
	//}
	//return false;
}

Parsed_params parse_params(int argc, const char** argv)
{
	Parsed_params parsed_params;
	parsed_params.operation = Parsed_params::USAGE;
    
    if ( (argc == 2) && (stricmp(argv[1], "run-tests")==0) ) 
		parsed_params.operation = Parsed_params::TEST;

    if ( (argc == 3) && (strcmp(argv[1], "-0")==0) ) 
		if (sscanf(argv[2], "%u", &parsed_params.pid) == 1) 
		     parsed_params.operation = Parsed_params::EXISTS;
    
    if ( (argc == 2) && (sscanf(argv[1], "%u", &parsed_params.pid) == 1)) 
        parsed_params.operation = Parsed_params::KILL;

    if ( (argc == 3) && (strcmp(argv[1], "-9")==0) ) 
		if (sscanf(argv[2], "%u", &parsed_params.pid) == 1) 
		     parsed_params.operation = Parsed_params::KILL;

    return parsed_params;
}

void test(BOOL condition, const char* msg, ...)
{
    va_list additional_params;

    va_start(additional_params, msg); // Initialize variable arguments.
    
	if (condition) 
	{
		printf(".");
	} 
	else 
	{   
		printf("\nFAILED: ");
        vprintf(msg, additional_params);
		printf("\n");
		exit(1);
	}
}

DWORD start_dummy_process()
{
	PROCESS_INFORMATION process_info;
	STARTUPINFO startup_info;
	memset(&startup_info, 0, sizeof(startup_info));

	test(CreateProcess(NULL, "C:\\windows\\notepad.exe", NULL, NULL, FALSE, NULL, NULL, NULL, &startup_info, &process_info),
		"Could not create dummy process!"); // "cmd.exe" is in "C:\\WINNT" on Win2k ...
	
	test(CloseHandle(process_info.hProcess), "could not close process handle");
	test(CloseHandle(process_info.hThread), "could not close thread handle");

	return process_info.dwProcessId;
}

void run_tests()
{
	DWORD pid;

	test(!known_process(99999), "should not have found bogus process");
	
	pid = start_dummy_process();

	test(known_process(pid), "could not find process %d", pid);

	test(kill_process(pid), "could not kill process %d", pid);

	test(!known_process(pid), "found process %d", pid);

	const char* params[10];
	params[0] = "kill";
	Parsed_params parsed_params = parse_params(1, params);
	test(parsed_params.operation == Parsed_params::USAGE, "parse usage");
  
	params[1] = "run-tests";
	parsed_params = parse_params(2, params);
	test(parsed_params.operation == Parsed_params::TEST, "parse test");
  
	params[1] = "-0";
	params[2] = "123";
	parsed_params = parse_params(3, params);
	test(parsed_params.operation == Parsed_params::EXISTS, "parse -0");
	test(parsed_params.pid == 123, "parse -0 pid");

	params[1] = "456";
	parsed_params = parse_params(2, params);
	test(parsed_params.operation == Parsed_params::KILL, "parse kill 456");
	test(parsed_params.pid == 456, "parse kill pid");

	params[1] = "-9";
	params[2] = "789";
	parsed_params = parse_params(3, params);
	test(parsed_params.operation == Parsed_params::KILL, "parse kill -9");
	test(parsed_params.pid == 789, "parse kill -9 pid");

    pid = start_dummy_process();
    char buf[10]; 
    itoa(pid, buf, 10);

	params[1] = "-0";
	params[2] = buf;
	test(main(3, params) == 0, "main could not find process %d", pid);

	params[1] = buf;
	test(main(2, params) == 0, "main could not kill process %d", pid);

	params[1] = "-0";
	params[2] = buf;
	test(main(3, params) != 0, "main found process %d", pid);

	printf("\nAll tests passed.\n");
}

int main(int argc, const char** argv)
{
	static int ret_val = -1;

	Parsed_params parsed_params = parse_params(argc, argv);
	switch (parsed_params.operation) {
		case Parsed_params::TEST: run_tests(); ret_val = 0; break;
		case Parsed_params::KILL: ret_val = kill_process(parsed_params.pid) ? 0 : 1; break;
		case Parsed_params::EXISTS: ret_val = known_process(parsed_params.pid) ? 0 : 1; break;
	}

	return ret_val;
}

// we should move the switch statement into the parsed_params struct.
