.class public WhileAndIF
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 5

	bipush 20
	istore_1

	bipush 10
	istore_2

	bipush 10
	newarray int
	astore 4

	iload_1
	iload_2
	if_icmpge if_1_else
	iload_1
	iconst_1
	isub
	istore_3

	goto if_1_end
if_1_else:
	iload_2
	iconst_1
	isub
	istore_3

if_1_end:
while_2_begin:
	iconst_0
	iconst_1
	isub
	iload_3
	if_icmpge while_2_end
	aload 4
	iload_3
	iload_1
	iload_2
	isub
	iastore

	iload_3
	iconst_1
	isub
	istore_3

	iload_1
	iconst_1
	isub
	istore_1

	iload_2
	iconst_1
	isub
	istore_2

	goto while_2_begin
while_2_end:
	iconst_0
	istore_3

while_3_begin:
	iload_3
	aload 4
	arraylength
	if_icmpge while_3_end
	aload 4
	iload_3
	iaload
	invokestatic io/println(I)V

	iinc 3 1

	goto while_3_begin
while_3_end:
	return
.end method


