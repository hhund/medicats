<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="links.xslt" />

	<xsl:template match="text()" />
	<xsl:template match="dictionary-release">
		<html>
			<head>
			</head>
			<body>
				<h1>
					<xsl:text>MEDICATS: </xsl:text>
					<xsl:value-of select="name" />
				</h1>
				<p>
					<xsl:text>OID: </xsl:text>
					<xsl:value-of select="oid" />
				</p>
				<xsl:apply-imports />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>