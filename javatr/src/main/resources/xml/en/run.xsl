<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
                <title style="text-transform:capitalize;">
                    Test Run Results
                </title>
            </head>
            <body>
                <h2 style="text-transform:capitalize;"><xsl:value-of select="/run/@type"/> Test Run Results
                </h2>
                <h3>Resume</h3>
                <h3>Groups List</h3>
                <table width="50%" border="1" cellspacing="0" cellpadding="5">
                    <tr>
                        <th>Group Name</th>
                        <th>Number of Groups</th>
                        <th>Result</th>
                        <th>Start</th>
                        <th>End</th>
                    </tr>
                    <xsl:for-each select="/run/suite">
                        <tr>
                            <td>
                                <a>
                                    <xsl:attribute name="href"><xsl:value-of select="@name"/>.xml</xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </a>
                            </td>
                            <td>
                                <xsl:value-of select="@num_of_groups"/>
                            </td>
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
            </body>
        </html>
    </xsl:template>
</xsl:transform>
