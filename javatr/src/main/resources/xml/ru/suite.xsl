<?xml version="1.0" encoding="UTF-8"?>
<xsl:transform version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
                <title>
                    <xsl:value-of select="/suite/@name"/>
                </title>
            </head>
            <body>
                <h2>Результаты прогона сьюта &quot;<xsl:value-of select="/suite/@name"/>&quot;
                </h2>
                <h3>Резюме</h3>
                <h3>Список групп тестов</h3>
                <table width="500" border="1" cellspacing="0">
                    <tr>
                        <th>Имя группы</th>
                        <th>ID</th>
                        <th>Количество тестов</th>
                        <th>Результат</th>
                        <th>Начало</th>
                        <th>Окончание</th>
                    </tr>
                    <xsl:for-each select="/suite/precondition">
                    <tr>
                        <td>
                            <a><xsl:attribute name="href">#suitepre</xsl:attribute>Suite precondition</a>
                        </td>
                        <td></td>
                        <td>1</td>
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
                                <xsl:value-of select="@id"/>
                            </td>
                            <td>
                                <xsl:value-of select="count(test)"/>
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
                    <xsl:for-each select="/suite/postcondition">
                    <tr>
                        <td>
                            <a><xsl:attribute name="href">#suitepost</xsl:attribute>Suite postcondition</a>
                        </td>
                        <td></td>
                        <td>1</td>
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


                <h4><a><xsl:attribute name="name">#suitepre</xsl:attribute>Действия перед запуском сьюта</a></h4>
                <table>
                <xsl:for-each select="/suite/precondition/step">
                    <tr><td><xsl:if test="action">Действие: <xsl:value-of select="action"/></xsl:if></td></tr>
                    <tr><td><xsl:if test="result">Результат: <xsl:value-of select="result"/></xsl:if></td></tr>
                    <tr><td><xsl:if test="error">Ошибка: <xsl:value-of select="error"/></xsl:if></td></tr>
                </xsl:for-each>
                </table>

                <br/>
                <xsl:for-each select="/suite/suite_results/test_group">
                <p>
                    <h3><a>
                        <xsl:attribute name="name">#group<xsl:number value="position()" format="1"/>
                        </xsl:attribute>
                        Результаты группы тестов <xsl:value-of select="@name"/>
                        </a>
                    </h3>
                    <table width="500" border="1" cellspacing="0">
                        <tr>
                            <th>№</th>
                            <th>Тестовые данные</th>
                            <th>Всего методов</th>
                            <th>Запущено методов</th>
                            <th>Результат</th>
                            <th>Начало</th>
                            <th>Окончание</th>
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
                            <td><xsl:value-of select="@test_data"/></td>
                            <td><xsl:value-of select="test_results/@num_of_methods"/></td>
                            <td><xsl:value-of select="count(test_results/method_results)"/></td>
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
                </p>
                </xsl:for-each>

                <xsl:for-each select="/suite/suite_results/test_group">
                    <xsl:for-each select="test">
                        <xsl:variable name="iter" select="position()"/>
                    <p>
                    <a>
                    <xsl:attribute name="name">#<xsl:value-of select="../@name"/><xsl:number value="position()" format="1"/>
                    </xsl:attribute>
                    <h3><xsl:number value="position()"/>. Результаты теста с данными: <xsl:value-of select="@test_data"/></h3>
                    </a>


                    <table width="500" border="1" cellspacing="0">
                        <tr>
                            <th>Тип метода</th>
                            <th>Метод</th>
                            <th>Результат</th>
                            <th>Начало</th>
                            <th>Окончание</th>
                        </tr>
                        <xsl:if test="precondition">
                        <tr>
                            <td>
                                <a>
                                    <xsl:attribute name="href">#<xsl:value-of select="../@name"/>_pre_<xsl:number value="position()" format="1"/></xsl:attribute>
                                    Предварительные действия
                                </a>
                            </td>
                            <td>Конструктор</td>
                            <xsl:if test="precondition/@result = 'PASSED'">
                                <td bgcolor="#99FF99">Пройден</td>
                            </xsl:if>
                            <xsl:if test="precondition/@result = 'FAILED'">
                                <td bgcolor="#FF9999">Провален</td>
                            </xsl:if>
                            <xsl:if test="precondition/@result = 'SKIPPED'">
                                <td bgcolor="#FFFF77">Пропущен</td>
                            </xsl:if>
                            <td><xsl:value-of select="precondition/@start"/></td>
                            <td><xsl:value-of select="precondition/@end"/></td>
                        </tr>
                        </xsl:if>
                        <xsl:for-each select="test_results/method_results">
                        <tr>
                            <td>
                                <a>
                                    <xsl:attribute name="href">#<xsl:value-of select="../../../@name"/>_<xsl:value-of select="$iter"/>_<xsl:value-of select="@name"/></xsl:attribute>
                                    Метод
                                </a>
                            </td>
                            <td><xsl:value-of select="@name"/></td>                            
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
                        <xsl:if test="postcondition">
                        <tr>
                            <td>
                                <a>
                                    <xsl:attribute name="href">#<xsl:value-of select="postcondition/@name"/>_post_<xsl:number value="position()" format="1"/></xsl:attribute>
                                    Заключительные действия
                                </a>
                            </td>
                            <td><xsl:value-of select="postcondition/method_results/@name"/></td>
                            <xsl:if test="postcondition/@result = 'PASSED'">
                                <td bgcolor="#99FF99">Пройден</td>
                            </xsl:if>
                            <xsl:if test="postcondition/@result = 'FAILED'">
                                <td bgcolor="#FF9999">Провален</td>
                            </xsl:if>
                            <xsl:if test="postcondition/@result = 'SKIPPED'">
                                <td bgcolor="#FFFF77">Пропущен</td>
                            </xsl:if>
                            <td><xsl:value-of select="postcondition/@start"/></td>
                            <td><xsl:value-of select="postcondition/@end"/></td>
                        </tr>
                        </xsl:if>
                    </table>

                    <h4>
                        <a>
                            <xsl:attribute name="name">#<xsl:value-of select="../@name"/>_pre_<xsl:number value="position()" format="1"/></xsl:attribute>
                            Шаги предварительных действий:
                        </a>
                    </h4>
                    <table>
                    <xsl:for-each select="precondition/step">
                        <tr><td><xsl:if test="action">Действие: <xsl:value-of select="action"/></xsl:if></td></tr>
                        <tr><td><xsl:if test="result">Результат: <xsl:value-of select="result"/></xsl:if></td></tr>
                        <tr><td><xsl:if test="error">Ошибка: <xsl:value-of select="error"/></xsl:if></td></tr>
                    </xsl:for-each>
                    </table>
                    <xsl:for-each select="test_results/method_results">
                        <h4>
                            <a>
                               <xsl:attribute name="name">#<xsl:value-of select="../../../@name"/>_<xsl:value-of select="$iter"/>_<xsl:value-of select="@name"/></xsl:attribute>
                                Шаги метода <xsl:value-of select="@name"/>:
                            </a>
                        </h4>
                        <table>
                        <xsl:for-each select="step">
                            <tr><td><xsl:if test="action">Действие: <xsl:value-of select="action"/></xsl:if></td></tr>
                            <tr><td><xsl:if test="result">Результат: <xsl:value-of select="result"/></xsl:if></td></tr>
                            <tr><td><xsl:if test="error">Ошибка: <xsl:value-of select="error"/></xsl:if></td></tr>
                        </xsl:for-each>
                        </table>
                    </xsl:for-each>

                    <h4>
                        <a>
                            <xsl:attribute name="name">#<xsl:value-of select="postcondition/@name"/>_post_<xsl:number value="position()" format="1"/></xsl:attribute>
                            Шаги заключительных действий:
                        </a>
                    </h4>
                    <table>
                    <xsl:for-each select="postcondition/method_results/step">
                        <tr><td><xsl:if test="action">Действие: <xsl:value-of select="action"/></xsl:if></td></tr>
                        <tr><td><xsl:if test="result">Результат: <xsl:value-of select="result"/></xsl:if></td></tr>
                        <tr><td><xsl:if test="error">Ошибка: <xsl:value-of select="error"/></xsl:if></td></tr>
                    </xsl:for-each>
                    </table>
                </p>
                </xsl:for-each>
                </xsl:for-each>
                <h4><a><xsl:attribute name="name">#suitepost</xsl:attribute>Действия после прогона сьюта</a></h4>
                <table>
                <xsl:for-each select="/suite/postcondition/step">
                    <tr><td><xsl:if test="action">Действие: <xsl:value-of select="action"/></xsl:if></td></tr>
                    <tr><td><xsl:if test="result">Результат: <xsl:value-of select="result"/></xsl:if></td></tr>
                    <tr><td><xsl:if test="error">Ошибка: <xsl:value-of select="error"/></xsl:if></td></tr>
                </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:transform>
