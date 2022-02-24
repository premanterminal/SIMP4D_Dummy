package com.dispenda.laporan.tree;

import java.util.Vector;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class LPBRekapTreeContentProvider implements ITreeContentProvider {
	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getChildren(Object parentElement) {
		Vector subcats = ((Node) parentElement).getSubCategories();
		return subcats == null ? new Object[0] : subcats.toArray();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null && inputElement instanceof Vector)
			return ((Vector) inputElement).toArray();
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return ((Node) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((Node) element).getSubCategories() != null;
	}
}
