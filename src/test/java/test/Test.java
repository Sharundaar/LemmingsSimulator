package test;

public class Test
{
	static class Parent
	{
		public void doSomething()
		{
			System.out.println("I am the Parent, and I do as I like");
		}
	}

	static class ChildA extends Parent
	{
		@Override
		public void doSomething()
		{
			System.out
					.println("I am a child named A, but I have my own ways, different from Parent");
		}
	}

	static class ChildB extends Parent
	{
		@Override
		public void doSomething()
		{
			System.out
					.println("I am a child named B, but I have my own ways, different from my Parent and my siblings");
		}
	}

	public static void main(
			final String[] args)
	{

		Parent p = new Parent();
		p.doSomething();

		p = new ChildA();
		if (p instanceof Parent)
			p.doSomething();

		p = new ChildB();
		p.doSomething();

	}
}
