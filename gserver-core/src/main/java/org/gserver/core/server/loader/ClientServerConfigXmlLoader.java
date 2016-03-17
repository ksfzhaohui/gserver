package org.gserver.core.server.loader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.gserver.core.server.config.ClientServerConfig;
import org.gserver.core.server.config.ServerInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 加载clientServer配置xml
 * 
 * @author zhaohui
 * 
 */
public class ClientServerConfigXmlLoader {

	private Logger log = Logger.getLogger(ClientServerConfigXmlLoader.class);

	public ClientServerConfig load(String file) {
		InputStream in = null;
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			in = new FileInputStream(file);
			Document doc = builder.parse(in);
			NodeList list = doc.getElementsByTagName("server");

			ClientServerConfig config = new ClientServerConfig();
			if (list.getLength() > 0) {
				Node node = list.item(0);
				NodeList childs = node.getChildNodes();

				for (int j = 0; j < childs.getLength(); j++) {
					if ("server-name".equals(childs.item(j).getNodeName())) {
						config.setName(childs.item(j).getTextContent());
					} else if ("server-id".equals(childs.item(j).getNodeName())) {
						config.setId(Integer.parseInt(childs.item(j)
								.getTextContent()));
					} else if ("connect-servers".equals(childs.item(j)
							.getNodeName())) {
						NodeList servers = childs.item(j).getChildNodes();
						for (int k = 0; k < servers.getLength(); k++)
							if ("connect-server".equals(servers.item(k)
									.getNodeName())) {
								Node line = servers.item(k);
								ServerInfo info = new ServerInfo();
								NodeList attrs = line.getChildNodes();
								for (int l = 0; l < attrs.getLength(); l++) {
									String nodeName = attrs.item(l)
											.getNodeName();
									String textContent = attrs.item(l)
											.getTextContent();
									if ("server-id".equals(nodeName)) {
										info.setId(Integer
												.parseInt(textContent));
									} else if ("server-ip".equals(nodeName)) {
										info.setIp(textContent);
									} else if ("server-port".equals(nodeName)) {
										info.setPort(Integer
												.parseInt(textContent));
									} else if ("connect-num".equals(nodeName)) {
										info.setConnectNum(Integer
												.parseInt(textContent));
									}
								}

								config.getConnectServers().add(info);
							}
					}
				}
			}

			in.close();

			log.info("load server config !");
			return config;
		} catch (Exception e) {
			this.log.error(e, e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}
}
