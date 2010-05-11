
/*
* Tigase Jabber/XMPP Server
* Copyright (C) 2004-2010 "Artur Hefczyc" <artur.hefczyc@tigase.org>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, version 3 of the License.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. Look for COPYING file in the top folder.
* If not, see http://www.gnu.org/licenses/.
*
* $Rev$
* Last modified by $Author$
* $Date$
 */
package tigase.server.amp.action;

//~--- non-JDK imports --------------------------------------------------------

import tigase.server.Packet;
import tigase.server.amp.ActionAbstract;
import tigase.server.amp.ActionResultsHandlerIfc;

import tigase.xml.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Apr 27, 2010 5:35:45 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev$
 */
public class Error extends ActionAbstract {
	private static final String name = "error";
	private static final String FAILED_RULES_PATH = "error/failed-rules";
	private static final Element UNDEF_CONDITION = new Element("undefined-condition",
		new String[] { "xmlns" }, new String[] { "urn:ietf:params:xml:ns:xmpp-stanzas" });
	private static final Element FAILED_RULES = new Element("failed-rules",
		new String[] { "xmlns" }, new String[] { "http://jabber.org/protocol/amp#errors" });
	private static final Element ERROR_TEMPLATE = new Element("error",
		new Element[] { UNDEF_CONDITION }, new String[] { "type",
			"code" }, new String[] { "modify", "500" });

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param packet
	 * @param rule
	 * @param resultsHandler
	 *
	 *
	 * @return
	 */
	@Override
	public boolean execute(Packet packet, Element rule, ActionResultsHandlerIfc resultsHandler) {
		Packet result = prepareAmpPacket(packet, rule);
		Element error = ERROR_TEMPLATE.clone();
		Element failed_rules = FAILED_RULES.clone();

		failed_rules.addChild(rule);
		error.addChild(failed_rules);
		result.getElement().addChild(error);
		resultsHandler.addOutPacket(result);

		return false;
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param params
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> getDefaults(Map<String, Object> params) {
		return null;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String getName() {
		return name;
	}

	//~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param props
	 */
	@Override
	public void setProperties(Map<String, Object> props) {}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
