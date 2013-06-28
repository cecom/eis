package com.geewhiz.eis.node.data.embedded;

import static com.geewhiz.eis.attributes.GeneralAttributes.NAME;
import static com.geewhiz.eis.attributes.GeneralAttributes.PK;
import static com.geewhiz.eis.attributes.UserAttributes.LDAP_ID;

import org.neo4j.graphdb.Node;

import com.geewhiz.eis.node.data.UserNodeData;

public class EmbeddedUserNodeData extends UserNodeData {

	public EmbeddedUserNodeData(Node node) {
		super((String) node.getProperty(PK), (String) node.getProperty(NAME));

		setLdapId((String) node.getProperty(LDAP_ID));
	}

}
