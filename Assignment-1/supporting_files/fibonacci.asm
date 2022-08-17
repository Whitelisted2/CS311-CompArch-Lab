	.data
n:
	5
	.text
main:
	load %x0, $n, %x3
	addi %x0, 0, %x4
	addi %x0, 1, %x5
	addi %x0, 65535, %x8
	bgt %x3, %x5, setfirst
	blt %x3, 2, getout
	end
setfirst:
	store %x4, 0, %x8
	subi %x8, 1, %x8
	bgt %x3, 2, nextpart
	end
nextpart:
	store %x5, 0, %x8
	subi %x8, 1, %x8
	addi %x0, 1, %x7
loop:
	addi %x7, 1, %x7
	beq %x7, %x3, getout
	add %x4, %x5, %x6
	add %x0, %x5, %x4
	add %x0, %x6, %x5
	store %x6, 0, %x8
	subi %x8, 1, %x8
	jmp loop
getout:
	end