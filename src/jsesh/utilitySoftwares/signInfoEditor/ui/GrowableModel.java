package jsesh.utilitySoftwares.signInfoEditor.ui;

public interface GrowableModel {

	/**
	 * Try to remove the preconditions.
	 * Returns false if not possible.
	 * @param row
	 */
	public abstract boolean removeRow(int row);

	public abstract void addRow(String code);

	/**
	 * Returns true if the  row can be removed.
	 * @param sel
	 * @return
	 */
	public abstract boolean canRemove(int row);

}