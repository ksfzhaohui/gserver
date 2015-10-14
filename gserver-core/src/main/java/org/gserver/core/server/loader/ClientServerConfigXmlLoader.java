package org.gserver.core.server.loader;

import java.io.FileInputStream;
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
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream in = new FileInputStream(file);
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
					} else if ("gate-servers".equals(childs.item(j)
							.getNodeName())) {
						NodeList servers = childs.item(j).getChildNodes();
						for (int k = 0; k < servers.getLength(); k++)
							if ("gate-server".equals(servers.item(k)
									.getNodeName())) {
								Node line = servers.item(k);
								ServerInfo info = new ServerInfo();
								NodeList attrs = line.getChildNodes();
								for (int l = 0; l < attrs.getLength(); l++) {
									if ("server-id".equals(attrs.item(l)
											.getNodeName()))
										info.setId(Integer.parseInt(attrs.item(
												l).getTextContent()));
									else if ("server-ip".equals(attrs.item(l)
											.getNodeName()))
										info.setIp(attrs.item(l)
												.getTextContent());
									else if ("server-port".equals(attrs.item(l)
											.getNodeName())) {
										info.setPort(Integer.parseInt(attrs
												.item(l).getTextContent()));
									}
								}

								config.getGateServers().add(info);
							}
					} else if ("world-server".equals(childs.item(j)
							.getNodeName())) {
						ServerInfo info = new ServerInfo();
						NodeList attrs = childs.item(j).getChildNodes();
						for (int l = 0; l < attrs.getLength(); l++) {
							if ("server-id".equals(attrs.item(l).getNodeName()))
								info.setId(Integer.parseInt(attrs.item(l)
										.getTextContent()));
							else if ("server-ip".equals(attrs.item(l)
									.getNodeName()))
								info.setIp(attrs.item(l).getTextContent());
							else if ("server-port".equals(attrs.item(l)
									.getNodeName())) {
								info.setPort(Integer.parseInt(attrs.item(l)
										.getTextContent()));
							}
						}

						config.setWorldServer(info);
					} else if ("public-server".equals(childs.item(j)
							.getNodeName())) {
						ServerInfo info = new ServerInfo();
						NodeList attrs = childs.item(j).getChildNodes();
						for (int l = 0; l < attrs.getLength(); l++) {
							if ("server-id".equals(attrs.item(l).getNodeName()))
								info.setId(Integer.parseInt(attrs.item(l)
										.getTextContent()));
							else if ("server-ip".equals(attrs.item(l)
									.getNodeName()))
								info.setIp(attrs.item(l).getTextContent());
							else if ("server-port".equals(attrs.item(l)
									.getNodeName())) {
								info.setPort(Integer.parseInt(attrs.item(l)
										.getTextContent()));
							}
						}

						config.setPublicServers(info);
					}
				}
			}

			in.close();

			log.info("load server config !");
			return config;
		} catch (Exception e) {
			this.log.error(e, e);
		}
		return null;
	}
}
