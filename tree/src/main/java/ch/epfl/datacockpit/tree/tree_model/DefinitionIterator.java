package ch.epfl.datacockpit.tree.tree_model;

import java.util.Iterator;


public abstract class DefinitionIterator implements Iterator<AbstractDefinition> {
	abstract void reset();
	abstract AbstractDefinition current();
	abstract boolean hasFutureNext();

}