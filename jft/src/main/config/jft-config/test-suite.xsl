<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
                <title>
                    <xsl:value-of select="/test-suite/test-case/@type"/> Test Suite
                </title>
            </head>
            <body>
                <table border="1" cellspacing="0" CELLPADDING="2" width="50%">
                    <xsl:for-each select="/test-suite/test-case">
                        <tr>
                            <td BGCOLOR="#CCCCCC" width="10%"><b>TEST CASE</b></td>
                            <td width="40%" colspan="2"><b>ID:</b>&#160;<xsl:value-of select="@id"/></td>
                        </tr>
                        <tr>
                            <td><b>Name</b></td>
                            <td colspan="2"><xsl:value-of select="@name"/></td>
                        </tr>
                        <tr>
                            <td><b>Type</b></td>
                            <td colspan="2"><xsl:value-of select="@type"/></td>
                        </tr>
                        <tr>
                            <td colspan="3"></td>
                        </tr>
                        <tr>
                            <td BGCOLOR="#CCCCCC" width="10%"><b>Field</b></td>
                            <td BGCOLOR="#CCCCCC" width="20%"><b>Value</b></td>
                            <td BGCOLOR="#CCCCCC" width="30%"><b>Description</b></td>
                        </tr>
                        <xsl:for-each select="field">

                            <tr>
                                <td>
                                    <xsl:value-of select="@type"/>&#160;"<b><xsl:value-of select="@name"/></b>"
                                </td>
                                <td>
                                    <xsl:value-of select="@value"/>
                                </td>
                                <td>
                                   <xsl:for-each select="requirement">
                                       <xsl:if test="@is-ok='false'">
                                           <font color="red">
                                               <xsl:value-of select="@idDesc"/>
                                           </font>
                                       </xsl:if>
                                       <xsl:if test="@is-ok='true'">
                                           <font color="green">
                                               <xsl:value-of select="@idDesc"/>
                                           </font>
                                       </xsl:if>
                                   </xsl:for-each>
                                </td>
                             </tr>
                        </xsl:for-each>
                        <tr>
                            <td width="100%" colspan="2">

                            </td>
                        </tr>
                        <xsl:if test="@type='POSITIVE'">
                            <tr>
                                <td>
                                    <b>Expected Result</b>
                                </td>
                                <td colspan="2">
                                    <b>PASSED</b>
                                </td>
                            </tr>
                        </xsl:if>
                        <xsl:if test="@type='SMOKE'">
                            <tr>
                                <td>
                                    <b>Expected Result</b>
                                </td>
                                <td colspan="2">
                                    <b>PASSED</b>
                                </td>
                            </tr>
                        </xsl:if>
                        <xsl:if test="@type='NEGATIVE'">
                            <tr>
                                <td>
                                    <b>Expected Result</b>
                                </td>
                                <td colspan="2">
                                    <b>FAILED</b>
                                </td>
                            </tr>
                        </xsl:if>
                        <tr>
                            <td width="100%" colspan="3">
                                &#160;
                            </td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:transform>
