<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="text()" />
	<xsl:template match="dictionary-releases">
		<html>
			<head>
			</head>
			<body>
				<h1>
					MEDICATS: Dictionary Releases
				</h1>
				<ul>
					<xsl:for-each select="dictionary-release">
						<li>
							<a href="{links/link/@href}" rel="{links/link/@rel}" type="{links/link/@type}">
								<xsl:text> </xsl:text>
								<xsl:value-of select="name" />
							</a>
							<xsl:if test="oid/text()">
								<xsl:text> 	(OID: </xsl:text>
								<xsl:value-of select="oid" />
								<xsl:text>)</xsl:text>
							</xsl:if>
						</li>
					</xsl:for-each>
				</ul>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>