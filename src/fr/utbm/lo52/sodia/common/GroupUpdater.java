package fr.utbm.lo52.sodia.common;

public final class GroupUpdater
{
	enum Operation
	{
		ADD("add"), REMOVE("remove");

		private String prefix;

		private Operation(String prefix)
		{
			this.prefix = prefix;
		}

		public String prefix()
		{
			return prefix;
		}

	}

	public static void magicUpdate(boolean contains, Object element,
			Object group)
	{
		boolean isAdd = (new Exception()).getStackTrace()[1].getMethodName()
				.startsWith(Operation.ADD.prefix());
		Operation operation = (isAdd ? Operation.ADD : Operation.REMOVE);
		if (isAdd ? (!contains) : contains)
		{
			try
			{
				group.getClass()
						.getMethod(operation.prefix(), element.getClass())
						.invoke(group, element);
				element.getClass()
						.getMethod(operation.prefix(), group.getClass())
						.invoke(element, group);
			} catch (Throwable t)
			{
				throw new IllegalArgumentException(
						"Parameters type doesn't implement "
								+ operation.prefix()
								+ "method with correct signature.", t);
			}
		}
	}
}
