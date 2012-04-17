package fr.utbm.lo52.sodia.logic;

public class Link
{
	public String alias;
	public String id;
	public Account<?> account;
	public Contact contact;
	
	@Override
	public boolean equals(Object o)
	{
		return equals((Link) o);
	}
	
	public boolean equals(Link link)
	{
		return this.id.equals(link.id) &&
				this.account.equals(link.account) &&
				this.contact.equals(link.contact);
	}
}
