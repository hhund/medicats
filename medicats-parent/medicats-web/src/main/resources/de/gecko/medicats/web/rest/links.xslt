<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="links">
		<h2>Links</h2>
		<ul>
			<xsl:for-each select="link">
				<li>
					<xsl:value-of select="@rel"></xsl:value-of>
					<xsl:text>: </xsl:text>
					<a href="{@href}" rel="{@rel}" type="{@type}">
						<xsl:value-of select="@title"></xsl:value-of>
					</a>
				</li>
			</xsl:for-each>
		</ul>
	</xsl:template>
</xsl:stylesheet>