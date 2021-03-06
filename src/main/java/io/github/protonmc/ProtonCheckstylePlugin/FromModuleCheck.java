package io.github.protonmc.ProtonCheckstylePlugin;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class FromModuleCheck extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[] {TokenTypes.METHOD_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        try {
            DetailAST modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
            if (modifiers.getChildCount(TokenTypes.ANNOTATION) < 1) return;
            DetailAST modifier = modifiers.findFirstToken(TokenTypes.ANNOTATION);
            boolean foundFromModule = false;
            boolean foundMixinMethod = false;
            while(modifier != null) {
                if(modifier.getType() != TokenTypes.ANNOTATION) break;
                if (modifier.findFirstToken(TokenTypes.IDENT).getText().contains("Inject") ||
                    modifier.findFirstToken(TokenTypes.IDENT).getText().contains("Redirect") ||
                    modifier.findFirstToken(TokenTypes.IDENT).getText().contains("ModifyArg") ||
                    modifier.findFirstToken(TokenTypes.IDENT).getText().contains("ModifyConstant") ||
                    modifier.findFirstToken(TokenTypes.IDENT).getText().contains("ModifyVariable")) {
                    foundMixinMethod = true;
                }
                if (modifier.findFirstToken(TokenTypes.IDENT).getText().contains("FromModule")) {
                    foundFromModule = true;
                }
                modifier = modifier.getNextSibling();
            }
            if (foundMixinMethod && !foundFromModule) {
                log(modifier.getLineNo(), "All Mixin injections have to have a @FromModule annotation!");
            }
        } catch (Throwable t) {
            System.out.println(t.toString());
        }
    }
}
