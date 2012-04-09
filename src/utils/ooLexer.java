package utils;
// $ANTLR 3.4 F:\\oo\\oo.g 2012-04-09 14:01:27

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class ooLexer extends Lexer {
    public static final int EOF=-1;
    public static final int AT=4;
    public static final int DOT=5;
    public static final int INDEX=6;
    public static final int L=7;
    public static final int R=8;
    public static final int WORD=9;

    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public ooLexer() {} 
    public ooLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public ooLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "F:\\oo\\oo.g"; }

    // $ANTLR start "WORD"
    public final void mWORD() throws RecognitionException {
        try {
            int _type = WORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:7:6: ( ( 'a' .. 'z' | 'A' .. 'Z' )+ )
            // F:\\oo\\oo.g:7:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
            {
            // F:\\oo\\oo.g:7:8: ( 'a' .. 'z' | 'A' .. 'Z' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= 'A' && LA1_0 <= 'Z')||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // F:\\oo\\oo.g:
            	    {
            	    if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WORD"

    // $ANTLR start "INDEX"
    public final void mINDEX() throws RecognitionException {
        try {
            int _type = INDEX;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:8:7: ( ( '0' .. '9' )+ )
            // F:\\oo\\oo.g:8:9: ( '0' .. '9' )+
            {
            // F:\\oo\\oo.g:8:9: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // F:\\oo\\oo.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "INDEX"

    // $ANTLR start "DOT"
    public final void mDOT() throws RecognitionException {
        try {
            int _type = DOT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:9:5: ( '.' )
            // F:\\oo\\oo.g:9:7: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "DOT"

    // $ANTLR start "L"
    public final void mL() throws RecognitionException {
        try {
            int _type = L;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:10:3: ( '[' )
            // F:\\oo\\oo.g:10:5: '['
            {
            match('['); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "L"

    // $ANTLR start "R"
    public final void mR() throws RecognitionException {
        try {
            int _type = R;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:11:3: ( ']' )
            // F:\\oo\\oo.g:11:5: ']'
            {
            match(']'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "R"

    // $ANTLR start "AT"
    public final void mAT() throws RecognitionException {
        try {
            int _type = AT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // F:\\oo\\oo.g:12:4: ( '@' )
            // F:\\oo\\oo.g:12:6: '@'
            {
            match('@'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "AT"

    public void mTokens() throws RecognitionException {
        // F:\\oo\\oo.g:1:8: ( WORD | INDEX | DOT | L | R | AT )
        int alt3=6;
        switch ( input.LA(1) ) {
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt3=1;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt3=2;
            }
            break;
        case '.':
            {
            alt3=3;
            }
            break;
        case '[':
            {
            alt3=4;
            }
            break;
        case ']':
            {
            alt3=5;
            }
            break;
        case '@':
            {
            alt3=6;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 3, 0, input);

            throw nvae;

        }

        switch (alt3) {
            case 1 :
                // F:\\oo\\oo.g:1:10: WORD
                {
                mWORD(); 


                }
                break;
            case 2 :
                // F:\\oo\\oo.g:1:15: INDEX
                {
                mINDEX(); 


                }
                break;
            case 3 :
                // F:\\oo\\oo.g:1:21: DOT
                {
                mDOT(); 


                }
                break;
            case 4 :
                // F:\\oo\\oo.g:1:25: L
                {
                mL(); 


                }
                break;
            case 5 :
                // F:\\oo\\oo.g:1:27: R
                {
                mR(); 


                }
                break;
            case 6 :
                // F:\\oo\\oo.g:1:29: AT
                {
                mAT(); 


                }
                break;

        }

    }


 

}