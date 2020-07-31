.class public SumDigits
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 7

	iconst_0
	istore 6

	iconst_0
	istore 5

	new SumDigitsHelper
	dup
	invokespecial SumDigitsHelper/<init>()V
	astore_1

	aload_1
	invokevirtual SumDigitsHelper/readNumber()I

	istore_2

	iload_2
	istore_3

	aload_1
	iload_3
	invokevirtual SumDigitsHelper/countDigits(I)I

	istore 4

while_1_begin:
	iload 6
	iload 4
	if_icmpge while_1_end
	iload 5
	aload_1
	iload_3
	invokevirtual SumDigitsHelper/getLastNumber(I)I

	iadd
	istore 5

	iload_3
	bipush 10
	idiv
	istore_3

	iinc 6 1

	goto while_1_begin
while_1_end:
	aload_1
	iload 5
	iload_2
	invokevirtual SumDigitsHelper/printResult(II)V

	return
.end method


