<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <xsl:template match="/catalog">
    <fo:root>

      <!-- Layout -->
      <fo:layout-master-set>
        <fo:simple-page-master master-name="A4"
                               page-width="21cm" page-height="29.7cm"
                               margin-top="1.5cm" margin-bottom="1.5cm"
                               margin-left="2cm" margin-right="2cm">
          <fo:region-body margin-top="1.5cm" margin-bottom="1.0cm"/>
          <fo:region-before extent="1.5cm"/>
          <fo:region-after extent="1.0cm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <!-- Page sequence -->
      <fo:page-sequence master-reference="A4">

        <!-- Header -->
        <fo:static-content flow-name="xsl-region-before">
          <fo:block text-align="center"
                    font-family="sans-serif"
                    font-size="12pt"
                    font-weight="bold"
                    color="#444444"
                    padding-top="6pt">
            <xsl:text>Car Catalog</xsl:text>
          </fo:block>
        </fo:static-content>

        <!-- Footer -->
        <fo:static-content flow-name="xsl-region-after">
          <fo:block text-align="center"
                    font-family="sans-serif"
                    font-size="10pt"
                    color="#777777">
            <fo:inline>Page </fo:inline><fo:page-number/><fo:inline> of </fo:inline>
            <fo:page-number-citation ref-id="end-of-doc"/>
          </fo:block>
        </fo:static-content>

        <!-- Body -->
        <fo:flow flow-name="xsl-region-body"
                 font-family="sans-serif"
                 font-size="11pt"
                 color="#222222">

          <!-- Title -->
          <fo:block text-align="center"
                    font-size="26pt"
                    font-weight="bold"
                    space-after="18pt"
                    color="#1f3350">
            Car Catalog
          </fo:block>

          <!-- Each car -->
          <xsl:for-each select="cars/car">
            <xsl:variable name="model"  select="/catalog/models/model[@id = current()/@modelRef]"/>
            <xsl:variable name="engine" select="/catalog/engines/engine[@id = current()/@engineRef]"/>
            <xsl:variable name="brand"  select="/catalog/brands/brand[@id = $model/@brandRef]"/>

            <!-- Each car starts on a new page -->
            <fo:block break-before="page" keep-together="always"
                      padding="20pt" margin-bottom="15pt"
                      background-color="#f6f7f9"
                      border="0.8pt solid #cccccc">

              <!-- image -->
              <fo:block text-align="center" space-after="12pt">
                <fo:external-graphic src="{@picture-link}" content-width="18cm" scaling="uniform"/>
              </fo:block>

              <!-- headline -->
              <fo:block font-size="15pt"
                        font-weight="bold"
                        color="#0f2133"
                        space-after="8pt">
                <xsl:value-of select="$brand/name"/>
                <fo:inline> — </fo:inline>
                <fo:inline font-weight="normal" color="#4b4b4b">
                  <xsl:value-of select="$model/name"/>
                </fo:inline>
              </fo:block>

              <!-- details -->
              <fo:block keep-together="always" line-height="1.6em" font-size="12pt">
                <fo:block><fo:inline font-weight="bold">Engine: </fo:inline>
                  <xsl:value-of select="$engine/name"/> (<xsl:value-of select="$engine/power"/>)
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Price: </fo:inline>
                  $<xsl:value-of select="format-number(price, '#,##0.00')"/>
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Description: </fo:inline>
                  <xsl:value-of select="description"/>
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Color: </fo:inline>
                  <xsl:value-of select="color/@value"/>
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Transmission: </fo:inline>
                  <xsl:value-of select="transmission/@type"/>
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Category: </fo:inline>
                  <xsl:value-of select="category/@type"/>
                </fo:block>

                <fo:block><fo:inline font-weight="bold">Year: </fo:inline>
                  <xsl:value-of select="year"/>
                </fo:block>
              </fo:block>
            </fo:block>
          </xsl:for-each>

          <!-- Page-number target -->
          <fo:block id="end-of-doc"/>

        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>

</xsl:stylesheet>
