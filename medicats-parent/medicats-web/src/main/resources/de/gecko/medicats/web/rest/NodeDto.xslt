<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="links.xslt" />

	<xsl:template match="text()" />
	<xsl:template match="icd|alpha-id|ops">
		<html>
			<head>
			</head>
			<body>
				<h1>
					<xsl:text>MEDICATS: </xsl:text>
					<xsl:value-of select="dictionary" />
					<xsl:text> </xsl:text>
					<xsl:value-of select="code" />
				</h1>
				<p>
					<xsl:text>Label: </xsl:text>
					<xsl:value-of select="label" />
				</p>
				<p>
					<xsl:text>OID: </xsl:text>
					<xsl:value-of select="oid" />
				</p>
				<p>
					<xsl:text>Code: </xsl:text>
					<xsl:value-of select="code" />
				</p>
				<xsl:if test="type/text()">
					<p>
						<xsl:text>Type: </xsl:text>
						<xsl:value-of select="type" />
					</p>
				</xsl:if>
				<xsl:apply-imports />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>