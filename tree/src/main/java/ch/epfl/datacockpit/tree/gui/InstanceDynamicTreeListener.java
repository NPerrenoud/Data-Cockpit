package ch.epfl.datacockpit.tree.gui;

import java.io.Serializable;

public interface InstanceDynamicTreeListener extends Serializable {
	public void treeReadyAction();
	public void treeNotReadyAction();
}
