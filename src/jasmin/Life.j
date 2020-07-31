.class public Life
.super java/lang/Object
.field private UNDERPOP_LIM I
.field private OVERPOP_LIM I
.field private REPRODUCE_NUM I
.field private LOOPS_PER_MS I
.field private xMax I
.field private yMax I
.field private field_name [I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 3

	new Life
	dup
	invokespecial Life/<init>()V
	astore_1

	aload_1
	invokevirtual Life/init()Z

	pop
while_1_begin:
	iconst_1
	ifeq while_1_end
	aload_1
	invokevirtual Life/printfield_name()Z

	pop
	aload_1
	invokevirtual Life/update()Z

	pop
	invokestatic io/read()I

	istore_2

	goto while_1_begin
while_1_end:
	return
.end method


.method public init()Z
	.limit stack 3
	.limit locals 3

	iconst_1
	newarray int
	astore_1

	aload_0
	iconst_2
	putfield Life/UNDERPOP_LIM I

	aload_0
	iconst_3
	putfield Life/OVERPOP_LIM I

	aload_0
	iconst_3
	putfield Life/REPRODUCE_NUM I

	aload_0
	ldc 225000
	putfield Life/LOOPS_PER_MS I

	aload_0
	aload_0
	aload_1
	invokevirtual Life/field_name([I)[I

	putfield Life/field_name [I

	aload_1
	iconst_0
	iaload
	istore_2

	aload_0
	iload_2
	iconst_1
	isub
	putfield Life/xMax I

	aload_0
	aload_0
	getfield Life/field_name [I
	arraylength
	iload_2
	idiv
	iconst_1
	isub
	putfield Life/yMax I

	iconst_1
	ireturn
.end method


.method public field_name([I)[I
	.limit stack 3
	.limit locals 3

	bipush 100
	newarray int
	astore_2

	aload_1
	iconst_0
	bipush 10
	iastore

	aload_2
	iconst_0
	iconst_0
	iastore

	aload_2
	iconst_1
	iconst_0
	iastore

	aload_2
	iconst_2
	iconst_1
	iastore

	aload_2
	iconst_3
	iconst_0
	iastore

	aload_2
	iconst_4
	iconst_0
	iastore

	aload_2
	iconst_5
	iconst_0
	iastore

	aload_2
	bipush 6
	iconst_0
	iastore

	aload_2
	bipush 7
	iconst_0
	iastore

	aload_2
	bipush 8
	iconst_0
	iastore

	aload_2
	bipush 9
	iconst_0
	iastore

	aload_2
	bipush 10
	iconst_1
	iastore

	aload_2
	bipush 11
	iconst_0
	iastore

	aload_2
	bipush 12
	iconst_1
	iastore

	aload_2
	bipush 13
	iconst_0
	iastore

	aload_2
	bipush 14
	iconst_0
	iastore

	aload_2
	bipush 15
	iconst_0
	iastore

	aload_2
	bipush 16
	iconst_0
	iastore

	aload_2
	bipush 17
	iconst_0
	iastore

	aload_2
	bipush 18
	iconst_0
	iastore

	aload_2
	bipush 19
	iconst_0
	iastore

	aload_2
	bipush 20
	iconst_0
	iastore

	aload_2
	bipush 21
	iconst_1
	iastore

	aload_2
	bipush 22
	iconst_1
	iastore

	aload_2
	bipush 23
	iconst_0
	iastore

	aload_2
	bipush 24
	iconst_0
	iastore

	aload_2
	bipush 25
	iconst_0
	iastore

	aload_2
	bipush 26
	iconst_0
	iastore

	aload_2
	bipush 27
	iconst_0
	iastore

	aload_2
	bipush 28
	iconst_0
	iastore

	aload_2
	bipush 29
	iconst_0
	iastore

	aload_2
	bipush 30
	iconst_0
	iastore

	aload_2
	bipush 31
	iconst_0
	iastore

	aload_2
	bipush 32
	iconst_0
	iastore

	aload_2
	bipush 33
	iconst_0
	iastore

	aload_2
	bipush 34
	iconst_0
	iastore

	aload_2
	bipush 35
	iconst_0
	iastore

	aload_2
	bipush 36
	iconst_0
	iastore

	aload_2
	bipush 37
	iconst_0
	iastore

	aload_2
	bipush 38
	iconst_0
	iastore

	aload_2
	bipush 39
	iconst_0
	iastore

	aload_2
	bipush 40
	iconst_0
	iastore

	aload_2
	bipush 41
	iconst_0
	iastore

	aload_2
	bipush 42
	iconst_0
	iastore

	aload_2
	bipush 43
	iconst_0
	iastore

	aload_2
	bipush 44
	iconst_0
	iastore

	aload_2
	bipush 45
	iconst_0
	iastore

	aload_2
	bipush 46
	iconst_0
	iastore

	aload_2
	bipush 47
	iconst_0
	iastore

	aload_2
	bipush 48
	iconst_0
	iastore

	aload_2
	bipush 49
	iconst_0
	iastore

	aload_2
	bipush 50
	iconst_0
	iastore

	aload_2
	bipush 51
	iconst_0
	iastore

	aload_2
	bipush 52
	iconst_0
	iastore

	aload_2
	bipush 53
	iconst_0
	iastore

	aload_2
	bipush 54
	iconst_0
	iastore

	aload_2
	bipush 55
	iconst_0
	iastore

	aload_2
	bipush 56
	iconst_0
	iastore

	aload_2
	bipush 57
	iconst_0
	iastore

	aload_2
	bipush 58
	iconst_0
	iastore

	aload_2
	bipush 59
	iconst_0
	iastore

	aload_2
	bipush 60
	iconst_0
	iastore

	aload_2
	bipush 61
	iconst_0
	iastore

	aload_2
	bipush 62
	iconst_0
	iastore

	aload_2
	bipush 63
	iconst_0
	iastore

	aload_2
	bipush 64
	iconst_0
	iastore

	aload_2
	bipush 65
	iconst_0
	iastore

	aload_2
	bipush 66
	iconst_0
	iastore

	aload_2
	bipush 67
	iconst_0
	iastore

	aload_2
	bipush 68
	iconst_0
	iastore

	aload_2
	bipush 69
	iconst_0
	iastore

	aload_2
	bipush 70
	iconst_0
	iastore

	aload_2
	bipush 71
	iconst_0
	iastore

	aload_2
	bipush 72
	iconst_0
	iastore

	aload_2
	bipush 73
	iconst_0
	iastore

	aload_2
	bipush 74
	iconst_0
	iastore

	aload_2
	bipush 75
	iconst_0
	iastore

	aload_2
	bipush 76
	iconst_0
	iastore

	aload_2
	bipush 77
	iconst_0
	iastore

	aload_2
	bipush 78
	iconst_0
	iastore

	aload_2
	bipush 79
	iconst_0
	iastore

	aload_2
	bipush 80
	iconst_0
	iastore

	aload_2
	bipush 81
	iconst_0
	iastore

	aload_2
	bipush 82
	iconst_0
	iastore

	aload_2
	bipush 83
	iconst_0
	iastore

	aload_2
	bipush 84
	iconst_0
	iastore

	aload_2
	bipush 85
	iconst_0
	iastore

	aload_2
	bipush 86
	iconst_0
	iastore

	aload_2
	bipush 87
	iconst_0
	iastore

	aload_2
	bipush 88
	iconst_0
	iastore

	aload_2
	bipush 89
	iconst_0
	iastore

	aload_2
	bipush 90
	iconst_0
	iastore

	aload_2
	bipush 91
	iconst_0
	iastore

	aload_2
	bipush 92
	iconst_0
	iastore

	aload_2
	bipush 93
	iconst_0
	iastore

	aload_2
	bipush 94
	iconst_0
	iastore

	aload_2
	bipush 95
	iconst_0
	iastore

	aload_2
	bipush 96
	iconst_0
	iastore

	aload_2
	bipush 97
	iconst_0
	iastore

	aload_2
	bipush 98
	iconst_0
	iastore

	aload_2
	bipush 99
	iconst_0
	iastore

	aload_2
	areturn
.end method


.method public update()Z
	.limit stack 4
	.limit locals 6

	aload_0
	getfield Life/field_name [I
	arraylength
	newarray int
	astore 5

	iconst_0
	istore_1

while_2_begin:
	iload_1
	aload_0
	getfield Life/field_name [I
	arraylength
	if_icmpge while_2_end
	aload_0
	getfield Life/field_name [I
	iload_1
	iaload
	istore_2

	aload_0
	iload_1
	invokevirtual Life/getLiveNeighborN(I)I

	istore_3

	iload_2
	iconst_1
	if_icmpge lessThan_4
	iconst_1
	goto lessThan_4_end
lessThan_4:
	iconst_0
lessThan_4_end:
	ifne if_3_else
	aload_0
	iload_3
	aload_0
	getfield Life/UNDERPOP_LIM I
	invokevirtual Life/ge(II)Z

	ifeq AND_5
	aload_0
	iload_3
	aload_0
	getfield Life/OVERPOP_LIM I
	invokevirtual Life/le(II)Z

	ifeq AND_5
	iconst_1
	goto AND_5_end
AND_5:
	iconst_0
AND_5_end:
	istore 4

	iload 4
	ifne if_6_else
	aload 5
	iload_1
	iconst_0
	iastore

	goto if_6_end
if_6_else:
	aload 5
	iload_1
	aload_0
	getfield Life/field_name [I
	iload_1
	iaload
	iastore

if_6_end:
	goto if_3_end
if_3_else:
	aload_0
	iload_3
	aload_0
	getfield Life/REPRODUCE_NUM I
	invokevirtual Life/eq(II)Z

	ifeq if_7_else
	aload 5
	iload_1
	iconst_1
	iastore

	goto if_7_end
if_7_else:
	aload 5
	iload_1
	aload_0
	getfield Life/field_name [I
	iload_1
	iaload
	iastore

if_7_end:
if_3_end:
	iinc 1 1

	goto while_2_begin
while_2_end:
	aload_0
	aload 5
	putfield Life/field_name [I

	iconst_1
	ireturn
.end method


.method public printfield_name()Z
	.limit stack 4
	.limit locals 3

	iconst_0
	istore_1

	iconst_0
	istore_2

while_8_begin:
	iload_1
	aload_0
	getfield Life/field_name [I
	arraylength
	if_icmpge while_8_end
	aload_0
	iload_2
	aload_0
	getfield Life/xMax I
	invokevirtual Life/gt(II)Z

	ifeq if_9_else
	invokestatic io/println()V

	iconst_0
	istore_2

	goto if_9_end
if_9_else:
if_9_end:
	aload_0
	getfield Life/field_name [I
	iload_1
	iaload
	invokestatic io/print(I)V

	iinc 1 1

	iinc 2 1

	goto while_8_begin
while_8_end:
	invokestatic io/println()V

	invokestatic io/println()V

	iconst_1
	ireturn
.end method


.method public trIdx(II)I
	.limit stack 3
	.limit locals 3

	iload_1
	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	iload_2
	imul
	iadd
	ireturn
.end method


.method public cartIdx(I)[I
	.limit stack 3
	.limit locals 6

	aload_0
	getfield Life/xMax I
	iconst_1
	iadd
	istore 4

	iload_1
	iload 4
	idiv
	istore_3

	iload_1
	iload_3
	iload 4
	imul
	isub
	istore_2

	iconst_2
	newarray int
	astore 5

	aload 5
	iconst_0
	iload_2
	iastore

	aload 5
	iconst_1
	iload_3
	iastore

	aload 5
	areturn
.end method


.method public getNeighborCoords(I)[I
	.limit stack 5
	.limit locals 10

	aload_0
	iload_1
	invokevirtual Life/cartIdx(I)[I

	astore 8

	aload 8
	iconst_0
	iaload
	istore_2

	aload 8
	iconst_1
	iaload
	istore_3

	iload_2
	aload_0
	getfield Life/xMax I
	if_icmpge if_10_else
	iload_2
	iconst_1
	iadd
	istore 6

	aload_0
	iload_2
	iconst_0
	invokevirtual Life/gt(II)Z

	ifeq if_11_else
	iload_2
	iconst_1
	isub
	istore 4

	goto if_11_end
if_11_else:
	aload_0
	getfield Life/xMax I
	istore 4

if_11_end:
	goto if_10_end
if_10_else:
	iconst_0
	istore 6

	iload_2
	iconst_1
	isub
	istore 4

if_10_end:
	iload_3
	aload_0
	getfield Life/yMax I
	if_icmpge if_12_else
	iload_3
	iconst_1
	iadd
	istore 7

	aload_0
	iload_3
	iconst_0
	invokevirtual Life/gt(II)Z

	ifeq if_13_else
	iload_3
	iconst_1
	isub
	istore 5

	goto if_13_end
if_13_else:
	aload_0
	getfield Life/yMax I
	istore 5

if_13_end:
	goto if_12_end
if_12_else:
	iconst_0
	istore 7

	iload_3
	iconst_1
	isub
	istore 5

if_12_end:
	bipush 8
	newarray int
	astore 9

	aload 9
	iconst_0
	aload_0
	iload_2
	iload 5
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	iconst_1
	aload_0
	iload 4
	iload 5
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	iconst_2
	aload_0
	iload 4
	iload_3
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	iconst_3
	aload_0
	iload 4
	iload 7
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	iconst_4
	aload_0
	iload_2
	iload 7
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	iconst_5
	aload_0
	iload 6
	iload 7
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	bipush 6
	aload_0
	iload 6
	iload_3
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	bipush 7
	aload_0
	iload 6
	iload 5
	invokevirtual Life/trIdx(II)I

	iastore

	aload 9
	areturn
.end method


.method public getLiveNeighborN(I)I
	.limit stack 4
	.limit locals 5

	iconst_0
	istore 4

	aload_0
	iload_1
	invokevirtual Life/getNeighborCoords(I)[I

	astore_2

	iconst_0
	istore_3

while_14_begin:
	iload_3
	aload_2
	arraylength
	if_icmpge while_14_end
	aload_0
	aload_0
	getfield Life/field_name [I
	aload_2
	iload_3
	iaload
	iaload
	iconst_0
	invokevirtual Life/ne(II)Z

	ifeq if_15_else
	iinc 4 1

	goto if_15_end
if_15_else:
if_15_end:
	iinc 3 1

	goto while_14_begin
while_14_end:
	iload 4
	ireturn
.end method


.method public busyWait(I)Z
	.limit stack 3
	.limit locals 4

	iload_1
	aload_0
	getfield Life/LOOPS_PER_MS I
	imul
	istore_3

	iconst_0
	istore_2

while_16_begin:
	iload_2
	iload_3
	if_icmpge while_16_end
	iinc 2 1

	goto while_16_begin
while_16_end:
	iconst_1
	ireturn
.end method


.method public eq(II)Z
	.limit stack 3
	.limit locals 3

	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z

	ifne negation_18
	iconst_1
	goto negation_18_end
negation_18:
	iconst_0
negation_18_end:
	ifeq AND_17
	aload_0
	iload_2
	iload_1
	invokevirtual Life/lt(II)Z

	ifne negation_19
	iconst_1
	goto negation_19_end
negation_19:
	iconst_0
negation_19_end:
	ifeq AND_17
	iconst_1
	goto AND_17_end
AND_17:
	iconst_0
AND_17_end:
	ireturn
.end method


.method public ne(II)Z
	.limit stack 3
	.limit locals 3

	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z

	ifne negation_20
	iconst_1
	goto negation_20_end
negation_20:
	iconst_0
negation_20_end:
	ireturn
.end method


.method public lt(II)Z
	.limit stack 2
	.limit locals 3

	iload_1
	iload_2
	if_icmpge lessThan_21
	iconst_1
	goto lessThan_21_end
lessThan_21:
	iconst_0
lessThan_21_end:
	ireturn
.end method


.method public le(II)Z
	.limit stack 3
	.limit locals 3

	aload_0
	iload_1
	iload_2
	invokevirtual Life/lt(II)Z

	ifne negation_24
	iconst_1
	goto negation_24_end
negation_24:
	iconst_0
negation_24_end:
	ifeq AND_23
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z

	ifne negation_25
	iconst_1
	goto negation_25_end
negation_25:
	iconst_0
negation_25_end:
	ifeq AND_23
	iconst_1
	goto AND_23_end
AND_23:
	iconst_0
AND_23_end:
	ifne negation_22
	iconst_1
	goto negation_22_end
negation_22:
	iconst_0
negation_22_end:
	ireturn
.end method


.method public gt(II)Z
	.limit stack 3
	.limit locals 3

	aload_0
	iload_1
	iload_2
	invokevirtual Life/le(II)Z

	ifne negation_26
	iconst_1
	goto negation_26_end
negation_26:
	iconst_0
negation_26_end:
	ireturn
.end method


.method public ge(II)Z
	.limit stack 3
	.limit locals 3

	aload_0
	iload_1
	iload_2
	invokevirtual Life/gt(II)Z

	ifne negation_29
	iconst_1
	goto negation_29_end
negation_29:
	iconst_0
negation_29_end:
	ifeq AND_28
	aload_0
	iload_1
	iload_2
	invokevirtual Life/eq(II)Z

	ifne negation_30
	iconst_1
	goto negation_30_end
negation_30:
	iconst_0
negation_30_end:
	ifeq AND_28
	iconst_1
	goto AND_28_end
AND_28:
	iconst_0
AND_28_end:
	ifne negation_27
	iconst_1
	goto negation_27_end
negation_27:
	iconst_0
negation_27_end:
	ireturn
.end method


