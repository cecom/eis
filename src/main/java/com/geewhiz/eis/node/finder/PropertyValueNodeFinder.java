package com.geewhiz.eis.node.finder;

import com.geewhiz.eis.node.data.PropertyValueNodeData;

public interface PropertyValueNodeFinder {

	public PropertyValueNodeData find(String deployment, String property, String environment);

	public PropertyValueNodeData findLatestIfExists(String property, String environment);

}
