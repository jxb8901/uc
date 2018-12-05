// kill.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#define FAIL(reason)  \
( \
	printf reason, \
	exit(1) \
)

typedef bool (*process_test_func)(HANDLE h_process);

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
	if (apply_to_process(process_id, kill_process_inner))
	{
		for(int i = 0; i < 500 && known_process(process_id); i++)
		{
			Sleep(50);
		}
		return !known_process(process_id);
	}
	return false;
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
        vprintf(msg, additional_params);
		exit(1);
	}
}

DWORD start_dummy_process()
{
	PROCESS_INFORMATION process_info;
	STARTUPINFO startup_info;
	memset(&startup_info, 0, sizeof(startup_info));

	test(CreateProcess(NULL, "C:\\windows\\system32\\cmd.exe", NULL, NULL, FALSE, NULL, NULL, NULL, &startup_info, &process_info),
		"Could not create dummy process!");
	
	test(CloseHandle(process_info.hProcess), "could not close process handle");
	test(CloseHandle(process_info.hThread), "could not close thread handle");

	return process_info.dwProcessId;
}

void run_tests()
{
	DWORD pid = start_dummy_process();

	test(known_process(pid), "could not find process %d", pid);

	test(kill_process(pid), "could not kill process %d", pid);

	//Sleep( 2000 );

	test(!known_process(pid), "found process %d", pid);

    // pid = start_dummy_process();
    

	printf("All tests passed.\n");
}

int _tmain(int argc, _TCHAR* argv[])
{
	run_tests();
	return 0;
}



