package cn.zmvision.ccm.factory.base.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.content.tagrules.html.Sm2TagRuleBundle;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {
	@Override
	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		/**
		 * 支持Page标签
		 */
		builder.addTagRuleBundle(new Sm2TagRuleBundle());
	}
}
