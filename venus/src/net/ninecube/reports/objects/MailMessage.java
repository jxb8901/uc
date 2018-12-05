/*
 * Copyright (C) 2003 Erik Swenson - eswenson@opensourcesoft.net
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *  
 */

package net.ninecube.reports.objects;

import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Vector;

import net.ninecube.reports.util.ByteArrayDataSource;

public class MailMessage implements Serializable
{
	private static final long serialVersionUID = -5816798771288286268L;

	private String sender;
	private String subject;
	private String text;

	private Vector recipients = new Vector();
	private Vector attachments = new Vector();
	private Vector htmlImageDataSources = new Vector();

	private ByteArrayDataSource byteArrayDataSource;

	public MailMessage()
	{
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public Vector getRecipients()
	{
		return recipients;
	}

	public void setRecipients(Vector recipients)
	{
		this.recipients = recipients;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public ByteArrayDataSource getByteArrayDataSource()
	{
		return byteArrayDataSource;
	}

	public void setByteArrayDataSource(ByteArrayDataSource byteArrayDataSource)
	{
		this.byteArrayDataSource = byteArrayDataSource;
	}

	public Vector getAttachments()
	{
		return attachments;
	}

	public void addAttachment(String fileName)
	{
		attachments.add(fileName);
	}

	public void addRecepient(String recipient)
	{
		recipients.add(recipient);
	}

	public String formatRecipients(String delimiter)
	{
		String addresses = "";

		for (int i = 0; i < recipients.size(); i++)
		{
			addresses += recipients.get(i) + delimiter;
		}

		return addresses.substring(0, addresses.length() - 1);
	}

	public void parseRecipients(String value)
	{
		StringTokenizer st = new StringTokenizer(value, "\t\n\r\f;,|");

		recipients = new Vector();

		while (st.hasMoreElements())
		{
			recipients.add(st.nextElement());
		}
	}

	public Vector getHtmlImageDataSources()
	{
		return htmlImageDataSources;
	}

	public void addHtmlImageDataSources(Vector htmlImageDataSources)
	{
		this.htmlImageDataSources.addAll(htmlImageDataSources);
	}

}
