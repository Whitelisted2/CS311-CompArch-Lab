	.data
a:
	10
	.text
main:
	load %x0, $a, %x3
	addi %x0, 2, %x4
	blt %x3, 2, noprime
	beq %x3, %x4, yesprime
loop:
	div %x3, %x4, %x5
	beq %x0, %x31, noprime
	addi %x4, 1, %x4
	beq %x4, %x3, yesprime
	bne %x4, %x3, loop
yesprime:
	addi %x0, 1, %x10
	end
noprime:
	subi %x0, 1, %x10
	end