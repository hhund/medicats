<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="links.xslt" />

	<xsl:template match="text()" />
	<xsl:template match="medicats">
		<html>
			<head>
			</head>
			<body>
				<h1>MEDICATS</h1>
				<p>Medical Classification and Terminology Systems Webservice - use
					Accept Header to get XML or JSON output.</p>
				<h2>Copyright</h2>
				<p>
					<ul>
						<li>
							<b>
								<xsl:text>EN: </xsl:text>
							</b>
							<xsl:value-of select="copyright-en" />
						</li>
						<li>
							<b>
								<xsl:text>DE: </xsl:text>
							</b>
							<xsl:value-of select="copyright-de" />
						</li>
					</ul>
				</p>
				<xsl:apply-imports />
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>