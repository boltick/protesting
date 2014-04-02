<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
	<xsl:template name="replace-text">
		<xsl:param name="text"/>
		<xsl:param name="replace" />
		<xsl:param name="by"  />

		<xsl:choose>
			<xsl:when test="contains($text, $replace)">
				<xsl:value-of select="substring-before($text, $replace)"/>
				<xsl:value-of select="$by" disable-output-escaping="yes"/>
				<xsl:call-template name="replace-text">
					<xsl:with-param name="text" select="substring-after($text, $replace)"/>
					<xsl:with-param name="replace" select="$replace" />
					<xsl:with-param name="by" select="$by" />
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="/">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
				<title>
					<xsl:value-of select="/suite/@name"/>
				</title>
			</head>
			<body>
				<h2>Suite Run Result &quot;<xsl:value-of select="/suite/@name"/>&quot;
				</h2>
				<h3>Resume</h3>
				<h3>List of Test Groups</h3>
				<table width="50%" border="1" cellspacing="0" cellpadding="5">
					<tr>
						<th>Group Name</th>
						<th>Grour_ID</th>
						<th>Number of Tests</th>
						<th>Result</th>
						<th>Start</th>
						<th>End</th>
					</tr>
					<xsl:for-each select="/suite/precondition">
						<tr>
							<td>
								<a><xsl:attribute name="href">#suitepre</xsl:attribute>Suite precondition</a>
							</td>
							<td>
								<xsl:value-of select="@group"/>
							</td>
							<td>1</td>
							<xsl:if test="@result = 'PASSED'">
								<td bgcolor="#99FF99">Passed</td>
							</xsl:if>
							<xsl:if test="@result = 'FAILED'">
								<td bgcolor="#FF9999">Failed</td>
							</xsl:if>
							<xsl:if test="@result = 'SKIPPED'">
								<td bgcolor="#FFFF77">Skipped</td>
							</xsl:if>
							<td>
								<xsl:value-of select="@start"/>
							</td>
							<td>
								<xsl:value-of select="@end"/>
							</td>
						</tr>
					</xsl:for-each>
					<xsl:for-each select="/suite/suite_results/test_group">
						<tr>
							<td>
								<a>
									<xsl:attribute name="href">#group<xsl:number value="position()" format="1"/>
									</xsl:attribute>
									<xsl:value-of select="@name"/>
								</a>
							</td>
							<td>
								<xsl:value-of select="@group"/>
							</td>
							<td>
								<xsl:value-of select="count(test)"/>
							</td>
							<xsl:if test="@result = 'PASSED'">
								<xsl:choose>
									<xsl:when test="@hidden = 'true'">
										<td bgcolor="#FFFFFF">HIDDEN</td>
									</xsl:when>
									<xsl:otherwise>
										<td bgcolor="#99FF99">Passed</td>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:if>
							<xsl:if test="@result = 'FAILED'">
								<td bgcolor="#FF9999">Failed</td>
							</xsl:if>
							<xsl:if test="@result = 'SKIPPED'">
								<td bgcolor="#FFFF77">Skipped</td>
							</xsl:if>
							<td>
								<xsl:value-of select="@start"/>
							</td>
							<td>
								<xsl:value-of select="@end"/>
							</td>
						</tr>
					</xsl:for-each>
					<xsl:for-each select="/suite/postcondition">
						<tr>
							<td>
								<a><xsl:attribute name="href">#suitepost</xsl:attribute>Suite postcondition</a>
							</td>
							<td>
								<xsl:value-of select="@group"/>
							</td>
							<td>1</td>
							<xsl:if test="@result = 'PASSED'">
								<td bgcolor="#99FF99">Passed</td>
							</xsl:if>
							<xsl:if test="@result = 'FAILED'">
								<td bgcolor="#FF9999">Failed</td>
							</xsl:if>
							<xsl:if test="@result = 'SKIPPED'">
								<td bgcolor="#FFFF77">Skipped</td>
							</xsl:if>
							<td>
								<xsl:value-of select="@start"/>
							</td>
							<td>
								<xsl:value-of select="@end"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>


				<h4><a><xsl:attribute name="name">suitepre</xsl:attribute></a>Steps of Precondition Action:</h4>
				<table width="50%">
					<xsl:for-each select="/suite/precondition/step">
						<tr><td><xsl:if test="action">Action: <xsl:value-of select="action"/></xsl:if></td></tr>
						<tr><td><xsl:if test="result">Result: <xsl:value-of select="result"/></xsl:if></td></tr>
						<tr><td><xsl:if test="error">Error: <xsl:value-of select="error"/></xsl:if></td></tr>
					</xsl:for-each>
				</table>

				<br/>
				<xsl:for-each select="/suite/suite_results/test_group">
					<p>
						<h3><a>
							<xsl:attribute name="name">group<xsl:number value="position()" format="1"/>
							</xsl:attribute>
							Test Results for Group "<xsl:value-of select="@name"/>"
						</a>
						</h3>
						<table width="50%" border="1" cellspacing="0">
							<tr>
								<th> # </th>
								<th>Test Data</th>
								<th>Methods</th>
								<th>Methods ran</th>
								<th>Result</th>
								<th>Start</th>
								<th>End</th>
							</tr>
							<xsl:for-each select="test">
								<tr>
									<td>
										<a>
											<xsl:attribute name="href">#<xsl:value-of select="../@name"/><xsl:number value="position()" format="1"/>
											</xsl:attribute>
											<xsl:number value="position()"/>
										</a>
									</td>
									<td width="50%"><font size="0.9"><xsl:value-of select="@test_data"/></font></td>
									<td><xsl:value-of select="test_results/@num_of_methods"/></td>
									<td><xsl:value-of select="count(test_results/method_results)"/></td>
									<xsl:if test="@result = 'PASSED'">
										<td bgcolor="#99FF99">Passed</td>
									</xsl:if>
									<xsl:if test="@result = 'FAILED'">
										<td bgcolor="#FF9999">Failed</td>
									</xsl:if>
									<xsl:if test="@result = 'SKIPPED'">
										<td bgcolor="#FFFF77">Skipped</td>
									</xsl:if>
									<td>
										<xsl:value-of select="@start"/>
									</td>
									<td>
										<xsl:value-of select="@end"/>
									</td>
								</tr>

							</xsl:for-each>
						</table>
					</p>
				</xsl:for-each>

				<xsl:for-each select="/suite/suite_results/test_group">
					<xsl:for-each select="test">
						<xsl:variable name="iter" select="position()"/>
						<p>
							<a>
								<xsl:attribute name="name"><xsl:value-of select="../@name"/><xsl:number value="position()" format="1"/>
								</xsl:attribute>
								<h3><xsl:number value="position()"/>. Test Case Result:
									<!--<xsl:value-of select="@test_data"/>-->
								</h3>
							</a>


							<table width="50%" border="1" cellspacing="0">
								<tr>
									<th>Method Type</th>
									<th>Method</th>
									<th>Result</th>
									<th>Start</th>
									<th>End</th>
								</tr>
								<xsl:if test="precondition">
									<xsl:for-each select="precondition/method_results">
										<tr>
											<td>
												<a>
													<xsl:attribute name="href">#<xsl:value-of select="../../../@name"/>_pre_<xsl:value-of select="$iter"/></xsl:attribute>
													Precondition
												</a>
											</td>
											<td><xsl:value-of select="@name"/></td>
											<xsl:if test="@result = 'PASSED'">
												<td bgcolor="#99FF99">Passed</td>
											</xsl:if>
											<xsl:if test="@result = 'FAILED'">
												<td bgcolor="#FF9999">Failed</td>
											</xsl:if>
											<xsl:if test="@result = 'SKIPPED'">
												<td bgcolor="#FFFF77">Skipped</td>
											</xsl:if>
											<td>
												<xsl:value-of select="@start"/>
											</td>
											<td>
												<xsl:value-of select="@end"/>
											</td>
										</tr>
									</xsl:for-each>
								</xsl:if>
								<xsl:for-each select="test_results/method_results">
									<tr>
										<td>
											<a>
												<xsl:attribute name="href">#<xsl:value-of select="../../../@name"/>_<xsl:value-of select="$iter"/>_<xsl:value-of select="@name"/></xsl:attribute>
												Test
											</a>
										</td>
										<td><xsl:value-of select="@name"/></td>
										<xsl:if test="@result = 'PASSED'">
											<td bgcolor="#99FF99">Passed</td>
										</xsl:if>
										<xsl:if test="@result = 'FAILED'">
											<td bgcolor="#FF9999">Failed</td>
										</xsl:if>
										<xsl:if test="@result = 'SKIPPED'">
											<td bgcolor="#FFFF77">Skipped</td>
										</xsl:if>
										<td>
											<xsl:value-of select="@start"/>
										</td>
										<td>
											<xsl:value-of select="@end"/>
										</td>
									</tr>

								</xsl:for-each>
								<xsl:if test="postcondition">
									<tr>
										<td>
											<a>
												<xsl:attribute name="href">#<xsl:value-of select="postcondition/@name"/>_post_<xsl:number value="position()" format="1"/></xsl:attribute>
												PostCondition
											</a>
										</td>
										<td><xsl:value-of select="postcondition/method_results/@name"/></td>
										<xsl:if test="postcondition/@result = 'PASSED'">
											<td bgcolor="#99FF99">Passed</td>
										</xsl:if>
										<xsl:if test="postcondition/@result = 'FAILED'">
											<td bgcolor="#FF9999">Failed</td>
										</xsl:if>
										<xsl:if test="postcondition/@result = 'SKIPPED'">
											<td bgcolor="#FFFF77">Skipped</td>
										</xsl:if>
										<td><xsl:value-of select="postcondition/@start"/></td>
										<td><xsl:value-of select="postcondition/@end"/></td>
									</tr>
								</xsl:if>
							</table>

							<xsl:for-each select="precondition/method_results">
								<h4>
									<a>
										<xsl:attribute name="name"><xsl:value-of select="../../../@name"/>_pre_<xsl:number value="position()" format="1"/></xsl:attribute>
										PreCondition steps:
									</a>
								</h4>
								<table width="50%">
									<xsl:for-each select="step">
										<tr>
											<td>
												<xsl:if test="action">
													<b><font color="blue">Action:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="action"  />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</xsl:if>
											</td>
										</tr>
										<xsl:for-each select="result">
											<tr>
												<td>
													<b><font color="green">Result:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="." />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="error">
											<tr>
												<td>
													<font color="red"><b>Result:</b>
														<xsl:call-template name="replace-text">
															<xsl:with-param name="text" select="."/>
															<xsl:with-param name="replace" select="'&#xD;'" />
															<xsl:with-param name="by" select="'&lt;br/&gt;'" />
														</xsl:call-template>
													</font>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="screen-shot">
											<tr>
												<td>
													<a>
														<xsl:attribute name="href"><xsl:value-of select="../screen-shot"/></xsl:attribute>
														<xsl:attribute name="target">_blank</xsl:attribute>
														<xsl:value-of select="screen-shot"/>
														<img>
															<xsl:attribute name="src"><xsl:value-of select="../screen-shot"/></xsl:attribute>
															<xsl:attribute name="alt"><xsl:value-of select="@title"/></xsl:attribute>
															<xsl:attribute name="width">50%</xsl:attribute>
														</img>
													</a>
												</td>
											</tr>
										</xsl:for-each>

									</xsl:for-each>

								</table>
							</xsl:for-each>

							<xsl:for-each select="test_results/method_results">
								<h4>
									<a>
										<xsl:attribute name="name"><xsl:value-of select="../../../@name"/>_<xsl:value-of select="$iter"/>_<xsl:value-of select="@name"/></xsl:attribute>
										Test steps <xsl:value-of select="@name"/>:
									</a>
								</h4>
								<table width="50%">
									<xsl:for-each select="step">
										<tr>
											<td>
												<xsl:if test="action">
													<b><font color="blue">Action:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="action"  />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</xsl:if>
											</td>
										</tr>
										<xsl:for-each select="result">
											<tr>
												<td>
													<b><font color="green">Result:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="." />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="error">
											<tr>
												<td>
													<font color="red"><b>Result:</b>
														<xsl:call-template name="replace-text">
															<xsl:with-param name="text" select="."/>
															<xsl:with-param name="replace" select="'&#xD;'" />
															<xsl:with-param name="by" select="'&lt;br/&gt;'" />
														</xsl:call-template>
													</font>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="screen-shot">
											<tr>
												<td>
													<a>
														<xsl:attribute name="href"><xsl:value-of select="../screen-shot"/></xsl:attribute>
														<xsl:attribute name="target">_blank</xsl:attribute>
														<xsl:value-of select="screen-shot"/>
														<img>
															<xsl:attribute name="src"><xsl:value-of select="../screen-shot"/></xsl:attribute>
															<xsl:attribute name="alt"><xsl:value-of select="@title"/></xsl:attribute>
															<xsl:attribute name="width">50%</xsl:attribute>
														</img>
													</a>
												</td>
											</tr>
										</xsl:for-each>
									</xsl:for-each>
								</table>
							</xsl:for-each>

							<xsl:for-each select="postcondition/method_results">
								<h4>
									<a>
										<xsl:attribute name="name"><xsl:value-of select="../@name"/>_post_<xsl:number value="position()" format="1"/></xsl:attribute>
										PostConditions steps:
									</a>
								</h4>

								<table width="50%">
									<xsl:for-each select="step">
										<tr>
											<td>
												<xsl:if test="action">
													<b><font color="blue">Action:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="action"  />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</xsl:if>
											</td>
										</tr>
										<xsl:for-each select="result">
											<tr>
												<td>
													<b><font color="green">Result:</font></b>
													<xsl:call-template name="replace-text">
														<xsl:with-param name="text" select="." />
														<xsl:with-param name="replace" select="'&#xD;'" />
														<xsl:with-param name="by" select="'&lt;br/&gt;'" />
													</xsl:call-template>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="error">
											<tr>
												<td>
													<font color="red"><b>Result:</b>
														<xsl:call-template name="replace-text">
															<xsl:with-param name="text" select="."/>
															<xsl:with-param name="replace" select="'&#xD;'" />
															<xsl:with-param name="by" select="'&lt;br/&gt;'" />
														</xsl:call-template>
													</font>
												</td>
											</tr>
										</xsl:for-each>
										<xsl:for-each select="screen-shot">
											<tr>
												<td>
													<a>
														<xsl:attribute name="href"><xsl:value-of select="../screen-shot"/></xsl:attribute>
														<xsl:attribute name="target">_blank</xsl:attribute>
														<xsl:value-of select="screen-shot"/>
														<img>
															<xsl:attribute name="src"><xsl:value-of select="../screen-shot"/></xsl:attribute>
															<xsl:attribute name="alt"><xsl:value-of select="@title"/></xsl:attribute>
															<xsl:attribute name="width">50%</xsl:attribute>
														</img>
													</a>
												</td>
											</tr>
										</xsl:for-each>

									</xsl:for-each>
								</table>
							</xsl:for-each>
						</p>
					</xsl:for-each>
				</xsl:for-each>
				<h4><a><xsl:attribute name="name">suitepost</xsl:attribute>Actions executed after test suite running</a></h4>
				<table width="50%">
					<xsl:for-each select="/suite/postcondition/step">
						<tr><td><xsl:if test="action">Action: <xsl:value-of select="action"/></xsl:if></td></tr>
						<xsl:for-each select="result">
							<tr><td>Result: <xsl:value-of select="."/></td></tr>
						</xsl:for-each>
						<tr><td><xsl:if test="error">Error: <xsl:value-of select="error"/></xsl:if></td></tr>
					</xsl:for-each>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:transform>
