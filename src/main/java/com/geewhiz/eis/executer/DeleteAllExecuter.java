package com.geewhiz.eis.executer;

import com.geewhiz.eis.node.manipulator.GraphManipulator;

public class DeleteAllExecuter implements EisExecuter {

	private GraphManipulator graphManipulator;

	public DeleteAllExecuter(GraphManipulator graphManipulator) {
		this.graphManipulator = graphManipulator;
	}

	@Override
	public void run() {
		graphManipulator.deleteAll();
	}
}
