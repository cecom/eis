package com.geewhiz.eis.node.data;

import static com.geewhiz.eis.attributes.DeploymentAttributes.ARTIFACT_URL;
import static com.geewhiz.eis.attributes.DeploymentAttributes.BUILD_ID;
import static com.geewhiz.eis.attributes.DeploymentAttributes.COMMIT;
import static com.geewhiz.eis.attributes.DeploymentAttributes.TAG;

import java.util.List;
import java.util.Map;

import com.geewhiz.eis.constants.Label;
import com.geewhiz.eis.constants.Relation;
import com.geewhiz.eis.node.data.container.RelationData;

public class DeploymentNodeData extends NamedNodeData {

	private String tag;
	private String commit;
	private String buildId;
	private String artifactUrl;

	private DeploymentNodeData successor;

	public DeploymentNodeData(String name) {
		super(name);
	}

	public DeploymentNodeData(String pk, String name) {
		super(pk, name);
	}

	public DeploymentNodeData(String pk, String name, DeploymentNodeData successor, String commit, String artifactUrl, String buildId, String tag) {
		super(pk, name);

		this.tag = tag;
		this.commit = commit;
		this.buildId = buildId;
		this.artifactUrl = artifactUrl;
		this.successor = successor;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCommit() {
		return commit;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getArtifactUrl() {
		return artifactUrl;
	}

	public void setArtifactUrl(String artifactUrl) {
		this.artifactUrl = artifactUrl;
	}

	public DeploymentNodeData getSuccessor() {
		return successor;
	}

	public void setSuccessor(DeploymentNodeData successor) {
		this.successor = successor;
	}

	@Override
	public Map<String, String> getAttributeValues() {
		Map<String, String> attributeValues = super.getAttributeValues();

		attributeValues.put(TAG, getTag());
		attributeValues.put(COMMIT, getCommit());
		attributeValues.put(BUILD_ID, getBuildId());
		attributeValues.put(ARTIFACT_URL, getArtifactUrl());

		return attributeValues;
	}

	@Override
	public List<RelationData> getRelations() {
		List<RelationData> relationData = super.getRelations();

		if (successor != null) {
			relationData.add(new RelationData(this, Relation.SUCCESSOR, successor));
		}

		return relationData;
	}

	@Override
	public Label getLabel() {
		return Label.DEPLOYMENT;
	}
}
