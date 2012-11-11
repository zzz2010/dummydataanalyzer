
public abstract class DummyWrapper implements AbstractDummy {

	AbstractDummy workerDummy;

	public DummyWrapper(AbstractDummy workerDummy) {
		super();
		this.workerDummy = workerDummy;
	}
	
	
}
