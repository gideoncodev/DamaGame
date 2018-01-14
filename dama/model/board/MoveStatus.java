package dama.model.board;

public enum MoveStatus {
	DONE {
		@Override
		boolean isDone() {
			return true;
		}
	};

	abstract boolean isDone();
}