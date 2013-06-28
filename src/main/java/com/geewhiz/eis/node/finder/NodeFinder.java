package com.geewhiz.eis.node.finder;

public interface NodeFinder<T> {

	T find(String name);

	T find(T data);

	T findIfExist(String name);

	T findIfExist(T data);

}
