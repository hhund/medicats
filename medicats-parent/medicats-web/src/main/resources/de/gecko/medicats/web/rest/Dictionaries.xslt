<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="text()" />
	<xsl:template match="dictionaries">
		<html>
			<head>
			</head>
			<body>
				<h1>
					MEDICATS: Dictionaries
				</h1>
				<ul>
					<xsl:for-each select="dictionary">
						<li>
							<a href="{links/link/@href}" rel="{links/link/@rel}" type="{links/link/@type}">
								<xsl:text> </xsl:text>
								<xsl:value-of select="name" />
							</a>
						</li>
					</xsl:for-each>
				</ul>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>