<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
                <title>
                    Результаты прогона набора тестов
                </title>
            </head>
            <body>
                <h2>Результаты прогона набора тестов
                </h2>
                <h3>Резюме</h3>
                <h3>Список наборов</h3>
                <table border="1" cellspacing="0">
                    <tr>
                        <th>Имя набора</th>
                        <th>Количество наборов</th>
                        <th>Результат</th>
                        <th>Начало</th>
                        <th>Окончание</th>
                    </tr>
                    <xsl:for-each select="/run/suite">
                        <tr>
                            <td>
                                <a>
                                    <xsl:attribute name="href"><xsl:value-of select="@name"/>.xml
                                    </xsl:attribute>
                                    <xsl:value-of select="@name"/>
                                </a>
                            </td>
                            <td>
                                <xsl:value-of select="@num_of_groups"/>
                            </td>
                            <xsl:if test="@result = 'PASSED'">
                                <td bgcolor="#99FF99">Пройден</td>
                            </xsl:if>
                            <xsl:if test="@result = 'FAILED'">
                                <td bgcolor="#FF9999">Провален</td>
                            </xsl:if>
                            <xsl:if test="@result = 'SKIPPED'">
                                <td bgcolor="#FFFF77">Пропущен</td>
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
