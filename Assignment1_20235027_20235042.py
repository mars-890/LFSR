"""
Done by Mohamed Tamer Younes
"""


# XOR function: Takes two bits and returns the result of XOR operation
def xor(x,y):
    if x==y:
        return '0'  # If bits are the same, return '0'
    else:
        return '1'  # If bits are different, return '1'


# LFSR class: Implements a Linear Feedback Shift Register
class LFSR:
    def __init__(self,seed,tap_position):
        """
        Initialize the LFSR with a seed and tap positions.
        :param seed: Binary string (e.g., "00110011")
        :param tap_position: List of tap positions (e.g., [0,1,2,3])
        """
        self.seed=seed  # Store the initial seed
        self.tap_position=tap_position  # Store the tap positions

    def generate_next_bit(self):
        """
        Generate the next bit using XOR feedback from tap positions.
        :return: The next bit (as a character, '0' or '1')
        """
        feedbackBit='0'  # Initialize feedback bit
        for tap in self.tap_position:  # Loop through each tap position
            # XOR the feedback bit with the bit at the tap position
            feedbackBit=xor(feedbackBit,self.seed[-(tap+1)])
        return feedbackBit  # Return the computed feedback bit

    def shift_register(self,newBit):
        """
        Shift the register and update the seed.
        :param newBit: The new bit to add to the left of the seed
        """
        self.seed=newBit+self.seed[:-1]  # Add new bit and remove the last bit

    def generate_sequence(self,length):
        """
        Generate a pseudorandom sequence of bits.
        :param length: Length of the sequence to generate
        :return: Generated sequence as a binary string
        """
        list=[]  # Initialize an empty list to store the sequence
        for _ in range(length):  # Loop to generate the required number of bits
            next_bit_generated=self.generate_next_bit()  # Generate the next bit
            list.append(next_bit_generated)  # Add the bit to the sequence
            self.shift_register(next_bit_generated)  # Update the register
        return ''.join(list)  # Convert the list to a string and return it


# XOR-based stream cipher function
def xor_stream_cipher(plaintext,keystream):
    """
    Encrypt/decrypt using XOR-based stream cipher.
    :param plaintext: Plaintext or ciphertext (as a binary string)
    :param keystream: Keystream (as a binary string)
    :return: Result of XOR operation (as a binary string)
    """
    answer=[]  # Initialize an empty list to store the result
    for i in range(len(plaintext)):  # Loop through each bit of the plaintext
        plainBit=plaintext[i]  # Get the current bit of the plaintext
        keyBit=keystream[i%len(keystream)]  # Get the corresponding keystream bit
        answerBit=xor(plainBit,keyBit)  # Perform XOR operation
        answer.append(answerBit)  # Add the result bit to the list
    return ''.join(answer)  # Convert the list to a string and return it


# Function to demonstrate LFSR pseudorandom sequence generation
def use_LFSR():
    """
    Demonstrate the generation of a pseudorandom sequence using LFSR.
    """
    # Ask user to enter seed (at most 5 characters)
    seed=input("Enter the seed in binary (at most 5 characters): ")
    while len(seed)>5 or not all(c in ('0','1') for c in seed):
        print("Invalid seed! Please enter a binary string of at most 5 characters.")
        seed=input("Enter the seed in binary (at most 5 characters): ")

    # Ask user to enter tap positions
    tap_position=input("Enter tap positions as comma-separated integers: ")
    tap_position=[int(tap) for tap in tap_position.split(',')]

    lfsr=LFSR(seed,tap_position)  # Initialize the LFSR
    list=lfsr.generate_sequence(100)  # Generate a sequence of 100 bits
    print("Generated Sequence (100 bits):",list)  # Print the generated sequence


# Function to demonstrate XOR-based stream cipher using LFSR
def use_stream_cipher():
    """
    Demonstrate encryption and decryption using an XOR-based stream cipher.
    """
    # Ask user to enter plain text
    plaintext=input("Enter plain text: ")

    # Convert plaintext to binary (8 bits per character)
    plaintextInBinary=''.join(format(ord(c),'08b') for c in plaintext)
    print("Plaintext in Binary:",plaintextInBinary)  # Print the binary plaintext

    # Ask user to enter seed (at most 5 characters)
    seed=input("Enter the seed in binary (at most 5 characters): ")
    while len(seed)>5 or not all(c in ('0','1') for c in seed):
        print("Invalid seed! Please enter a binary string of at most 5 characters.")
        seed=input("Enter the seed in binary (at most 5 characters): ")

    # Ask user to enter tap positions
    tap_position=input("Enter tap positions as comma-separated integers: ")
    tap_position=[int(tap) for tap in tap_position.split(',')]

    lfsr=LFSR(seed,tap_position)  # Initialize the LFSR
    keyStream=lfsr.generate_sequence(len(plaintextInBinary))  # Generate keystream
    print("Keystream:",keyStream)  # Print the keystream

    # Encrypt the plaintext using the keystream
    cipherText=xor_stream_cipher(plaintextInBinary,keyStream)
    print("Ciphertext:",cipherText)  # Print the ciphertext

    # Decrypt the ciphertext using the same keystream
    decryption=xor_stream_cipher(cipherText,keyStream)
    print("Decryption:",decryption)  # Print the decrypted binary

    # Convert decrypted binary back to text
    decrypted_text=''.join(chr(int(decryption[i:i+8],2)) for i in range(0,len(decryption),8))
    print("Decrypted Text:",decrypted_text)  # Print the decrypted text


# Main program
if __name__=="__main__":
    print("LFSR Pseudorandom Sequence Generation will be generated now")
    use_LFSR()  # Run the LFSR demonstration

    print("\nStream Cipher will be generated now")
    use_stream_cipher()  # Run the stream cipher demonstration
