.class public GuessNumber
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 7

	new HelperClassGuessNumber
	dup
	iconst_0
	bipush 10
	invokespecial HelperClassGuessNumber/<init>(ZI)V
	astore_1

	iconst_0
	istore_2

	iconst_0
	istore 6

	aload_1
	invokevirtual HelperClassGuessNumber/getNumberToGuess()I

	istore_3

	bipush 10
	istore 4

while_1_begin:
	iload 6
	ifne while_1_end
	aload_1
	invokevirtual HelperClassGuessNumber/getGuessedNumber()I

	istore 5

	iinc 2 1

	iload 5
	iload_3
	if_icmpge if_2_else
	aload_1
	invokevirtual HelperClassGuessNumber/printLowGuess()V

	goto if_2_end
if_2_else:
	iload 5
	iload_3
	if_icmpge lessThan_5
	iconst_1
	goto lessThan_5_end
lessThan_5:
	iconst_0
lessThan_5_end:
	ifne negation_4
	iconst_1
	goto negation_4_end
negation_4:
	iconst_0
negation_4_end:
	ifeq if_3_else
	iload_3
	iload 5
	if_icmpge lessThan_6
	iconst_1
	goto lessThan_6_end
lessThan_6:
	iconst_0
lessThan_6_end:
	ifeq if_3_else
	aload_1
	invokevirtual HelperClassGuessNumber/highLowGuess()V

	goto if_3_end
if_3_else:
	iconst_1
	istore 6

if_3_end:
if_2_end:
	iload 4
	iload_2
	if_icmpge if_7_else
	iconst_1
	istore 6

	goto if_7_end
if_7_else:
if_7_end:
	goto while_1_begin
while_1_end:
	iload_2
	iload 4
	if_icmpge if_8_else
	aload_1
	iload_2
	invokevirtual HelperClassGuessNumber/printWon(I)V

	goto if_8_end
if_8_else:
	aload_1
	iload_2
	invokevirtual HelperClassGuessNumber/printLost(I)V

if_8_end:
	return
.end method


