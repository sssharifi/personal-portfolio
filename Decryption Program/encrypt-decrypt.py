import itertools
import string
import sys
import textwrap
from sys import argv

####################################################################################################
# Vienere encryption and decryption functions
####################################################################################################

def vigenere(plaintext, key, a_is_zero=True):
    key = key.lower()
    if not all(k in string.ascii_lowercase for k in key):
        raise ValueError("Invalid key {!r}; the key can only consist of English letters".format(key))
    key_iter = itertools.cycle(map(ord, key))
    return "".join(
        chr(ord('a') + (
            (next(key_iter) - ord('a') + ord(letter) - ord('a'))    # Calculate shifted value
            + (0 if a_is_zero else 2)                               # Account for non-zero indexing
            ) % 26) if letter in string.ascii_lowercase             # Ignore non-alphabetic chars
        else letter
        for letter in plaintext.lower()
    )

def vigenere_decrypt(ciphertext, key, a_is_zero=True):
    # Decryption is encryption with the inverse key
    key_ind = [ord(k) - ord('a') for k in key.lower()]
    inverse = "".join(chr(ord('a') +
            ((26 if a_is_zero else 22) -
                (ord(k) - ord('a'))
            ) % 26) for k in key)
    return vigenere(ciphertext, inverse, a_is_zero)

def test_vigenere(text, key, a_is_zero=True):
    ciphertext = vigenere(text, key, a_is_zero)
    plaintext  = vigenere_decrypt(ciphertext, key, a_is_zero)
    assert plaintext == text, "{!r} -> {!r} -> {!r} (a {}= 0)".format(
        text, ciphertext, plaintext, "" if a_is_zero else "!")

# Test that the Vigenere encrypt and decrypt work (or are at least inverses)
for text in ["rewind", "text with spaces", "pun.ctuation", "numb3rs"]:
    for key in ["iepw", "aceaq", "safe", "pwa"]:
        test_vigenere(text, key, True)
        test_vigenere(text, key, False)

# Now that we're sure that all the vigenere stuff is working...

####################################################################################################
# Cipher solver
####################################################################################################

# From http://code.activestate.com/recipes/142813-deciphering-caesar-code/
ENGLISH_FREQ = (0.0749, 0.0129, 0.0354, 0.0362, 0.1400, 0.0218, 0.0174, 0.0422, 0.0665, 0.0027, 0.0047,
                0.0357, 0.0339, 0.0674, 0.0737, 0.0243, 0.0026, 0.0614, 0.0695, 0.0985, 0.0300, 0.0116,
                0.0169, 0.0028, 0.0164, 0.0004)

def compare_freq(text):
    """
    Compare the letter distribution of the given text with normal English. Lower is closer.
    Performs a simple sum of absolute difference for each letter
    """
    if not text:
        return None
    text = [t for t in text.lower() if t in string.ascii_lowercase]
    freq = [0] * 26
    total = float(len(text))
    for l in text:
        freq[ord(l) - ord('a')] += 1
    return sum(abs(f / total - E) for f, E in zip(freq, ENGLISH_FREQ))


def solve_vigenere(text, key_min_size=None, key_max_size=None, a_is_zero=True):
    """  
    """
    best_keys = []
    key_min_size = key_min_size or 1
    key_max_size = key_max_size or 20

    text_letters = [c for c in text.lower() if c in string.ascii_lowercase]

    for key_length in range(key_min_size, key_max_size):
        # Try all possible key lengths
        key = [None] * key_length
        for key_index in range(key_length):
            letters = "".join(itertools.islice(text_letters, key_index, None, key_length))
            shifts = []
            for key_char in string.ascii_lowercase:
                shifts.append(
                    (compare_freq(vigenere_decrypt(letters, key_char, a_is_zero)), key_char)
                )
            key[key_index] = min(shifts, key=lambda x: x[0])[1]
        best_keys.append("".join(key))
    best_keys.sort(key=lambda key: compare_freq(vigenere_decrypt(text, key, a_is_zero)))
    return best_keys[:2]

# CIPHERTEXT = sys.stdin.read().strip()

script, filename = argv
file = open(filename)
CIPHERTEXT = file.read()

print("Solving Vigenere cipher:")
print("*" * 80)
print(textwrap.fill(CIPHERTEXT, 80))
print("*" * 80)
for key in reversed(solve_vigenere(CIPHERTEXT)):
    print("")
    print("Found key: {!r}".format(key))
    print("Solution:")
    print("=" * 80)
    print(textwrap.fill(vigenere_decrypt(CIPHERTEXT, key)))
    print("=" * 80)