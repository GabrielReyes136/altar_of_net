package com.altar_of_net;

public class EightBall {
    private int my_mod;
    private String [] answers;
    public EightBall( String[] EnterParams, int my_mod) {
    answers = EnterParams;
    this.my_mod = my_mod;
    }
    public EightBall() {
        this( new String[] { "It is certain.",
                "It is decidedly so.",
                "Without a doubt.",
                "Yes definitely.",
                "You may rely on it.",
                "As i see it, yes.",
                "Most likely.",
                "Outlook good.",
                "Yes.",
                "Sings point to yes.",
                "Reply hazy, try again.",
                "Ask again later.",
                "Better not yell you now.",
                "Cannot predict now.",
                "Concentrate and ask again.",
                "Don't count on it.",
                "My reply is no.",
                "My sources say no.",
                "Outlook not so good.",
                "Very doubtful."}, 20 );
    }
    public String getAnswer(int index) {
        if ( this.answers != null) {
            return this.answers[index % this.my_mod];
        }
        return "Cannot predict now";
    }
}
