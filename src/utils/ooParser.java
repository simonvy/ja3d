package utils;
// $ANTLR 3.4 F:\\oo\\oo.g 2012-04-09 14:01:27

import org.antlr.runtime.*;

import ja3d.loaders.Parser;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.debug.*;
import java.io.IOException;
@SuppressWarnings({"all", "warnings", "unchecked"})
public class ooParser extends DebugParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AT", "DOT", "INDEX", "L", "R", "WORD"
    };

    public static final int EOF=-1;
    public static final int AT=4;
    public static final int DOT=5;
    public static final int INDEX=6;
    public static final int L=7;
    public static final int R=8;
    public static final int WORD=9;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


public static final String[] ruleNames = new String[] {
    "invalidRule", "path", "property"
};

public static final boolean[] decisionCanBacktrack = new boolean[] {
    false, // invalid decision
    false, false, false
};

 
    public int ruleLevel = 0;
    public int getRuleLevel() { return ruleLevel; }
    public void incRuleLevel() { ruleLevel++; }
    public void decRuleLevel() { ruleLevel--; }
    public ooParser(TokenStream input) {
        this(input, DebugEventSocketProxy.DEFAULT_DEBUGGER_PORT, new RecognizerSharedState());
    }
    public ooParser(TokenStream input, int port, RecognizerSharedState state) {
        super(input, state);
        DebugEventSocketProxy proxy =
            new DebugEventSocketProxy(this, port, null);

        setDebugListener(proxy);
        try {
            proxy.handshake();
        }
        catch (IOException ioe) {
            reportError(ioe);
        }
    }

public ooParser(TokenStream input, DebugEventListener dbg) {
    super(input, dbg, new RecognizerSharedState());
}

protected boolean evalPredicate(boolean result, String predicate) {
    dbg.semanticPredicate(result, predicate);
    return result;
}

    public String[] getTokenNames() { return ooParser.tokenNames; }
    public String getGrammarFileName() { return "F:\\oo\\oo.g"; }



    // $ANTLR start "path"
    // F:\\oo\\oo.g:3:1: path : WORD ( property )* ;
    public final void path() throws RecognitionException {
        try { dbg.enterRule(getGrammarFileName(), "path");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(3, 0);

        try {
            // F:\\oo\\oo.g:3:6: ( WORD ( property )* )
            dbg.enterAlt(1);

            // F:\\oo\\oo.g:3:8: WORD ( property )*
            {
            dbg.location(3,8);
            match(input,WORD,FOLLOW_WORD_in_path10); 
            dbg.location(3,13);
            // F:\\oo\\oo.g:3:13: ( property )*
            try { dbg.enterSubRule(1);

            loop1:
            do {
                int alt1=2;
                try { dbg.enterDecision(1, decisionCanBacktrack[1]);

                int LA1_0 = input.LA(1);

                if ( (LA1_0==DOT) ) {
                    alt1=1;
                }


                } finally {dbg.exitDecision(1);}

                switch (alt1) {
            	case 1 :
            	    dbg.enterAlt(1);

            	    // F:\\oo\\oo.g:3:13: property
            	    {
            	    dbg.location(3,13);
            	    pushFollow(FOLLOW_property_in_path12);
            	    property();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);
            } finally {dbg.exitSubRule(1);}


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        dbg.location(3, 21);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "path");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return ;
    }
    // $ANTLR end "path"



    // $ANTLR start "property"
    // F:\\oo\\oo.g:5:1: property : DOT ( AT )? WORD ( L INDEX R )? ;
    public final void property() throws RecognitionException {
        try { dbg.enterRule(getGrammarFileName(), "property");
        if ( getRuleLevel()==0 ) {dbg.commence();}
        incRuleLevel();
        dbg.location(5, 0);

        try {
            // F:\\oo\\oo.g:5:9: ( DOT ( AT )? WORD ( L INDEX R )? )
            dbg.enterAlt(1);

            // F:\\oo\\oo.g:5:11: DOT ( AT )? WORD ( L INDEX R )?
            {
            dbg.location(5,11);
            match(input,DOT,FOLLOW_DOT_in_property20); 
            dbg.location(5,15);
            // F:\\oo\\oo.g:5:15: ( AT )?
            int alt2=2;
            try { dbg.enterSubRule(2);
            try { dbg.enterDecision(2, decisionCanBacktrack[2]);

            int LA2_0 = input.LA(1);

            if ( (LA2_0==AT) ) {
                alt2=1;
            }
            } finally {dbg.exitDecision(2);}

            switch (alt2) {
                case 1 :
                    dbg.enterAlt(1);

                    // F:\\oo\\oo.g:5:15: AT
                    {
                    dbg.location(5,15);
                    match(input,AT,FOLLOW_AT_in_property22); 

                    }
                    break;

            }
            } finally {dbg.exitSubRule(2);}

            dbg.location(5,19);
            match(input,WORD,FOLLOW_WORD_in_property25); 
            dbg.location(5,24);
            // F:\\oo\\oo.g:5:24: ( L INDEX R )?
            int alt3=2;
            try { dbg.enterSubRule(3);
            try { dbg.enterDecision(3, decisionCanBacktrack[3]);

            int LA3_0 = input.LA(1);

            if ( (LA3_0==L) ) {
                alt3=1;
            }
            } finally {dbg.exitDecision(3);}

            switch (alt3) {
                case 1 :
                    dbg.enterAlt(1);

                    // F:\\oo\\oo.g:5:25: L INDEX R
                    {
                    dbg.location(5,25);
                    match(input,L,FOLLOW_L_in_property28); 
                    dbg.location(5,27);
                    match(input,INDEX,FOLLOW_INDEX_in_property30); 
                    dbg.location(5,33);
                    match(input,R,FOLLOW_R_in_property32); 

                    }
                    break;

            }
            } finally {dbg.exitSubRule(3);}


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        dbg.location(5, 35);

        }
        finally {
            dbg.exitRule(getGrammarFileName(), "property");
            decRuleLevel();
            if ( getRuleLevel()==0 ) {dbg.terminate();}
        }

        return ;
    }
    // $ANTLR end "property"

    // Delegated rules


 

    public static final BitSet FOLLOW_WORD_in_path10 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_property_in_path12 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_DOT_in_property20 = new BitSet(new long[]{0x0000000000000210L});
    public static final BitSet FOLLOW_AT_in_property22 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_WORD_in_property25 = new BitSet(new long[]{0x0000000000000082L});
    public static final BitSet FOLLOW_L_in_property28 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_INDEX_in_property30 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_R_in_property32 = new BitSet(new long[]{0x0000000000000002L});

}