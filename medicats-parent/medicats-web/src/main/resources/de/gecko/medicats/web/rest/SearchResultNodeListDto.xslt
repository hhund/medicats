<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="text()" />
	<xsl:template match="search-result">
		<html>
			<head>
			</head>
			<body>
				<h1>
					MEDICATS: Full Text Search
				</h1>
				<p>
					<form action="" method="get">
						Search:
						<input type="text" name="q" />
						<input type="submit" value="Submit" />
					</form>
				</p>
				<ul>
					<xsl:for-each select="result">
						<li>
							<p>
								<a href="{links/link/@href}" rel="{links/link/@rel}" type="{links/link/@type}">
									<xsl:value-of select="label" />
								</a>
								<br />
								<span>
									<xsl:value-of select="dictionary" />
									<xsl:if test="oid/text()">
										<xsl:text> (OID: </xsl:text>
										<xsl:value-of select="oid" />
										<xsl:text>) </xsl:text>
									</xsl:if>
									<xsl:text> Code: </xsl:text>
									<xsl:value-of select="code" />
								</span>
							</p>
						</li>
					</xsl:for-each>
					<xsl:if test="not(result)">
						<li>
							<xsl:text><i>No Result</i></xsl:text>
						</li>
					</xsl:if>
				</ul>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>