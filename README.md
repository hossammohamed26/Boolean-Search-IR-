# Boolean Search Algorithm (Java)

This repository contains an implementation of a Boolean search algorithm in Java. The algorithm allows users to perform efficient searches on a collection of documents using boolean expressions.

## Usage

To use the boolean search algorithm, follow these steps:

1. Clone the repository to your local machine:

   ```
   git clone https://github.com/hossammohamed26/Boolean-Search-IR-
   ```

2. Open the project in your preferred Java IDE.

3. Prepare your document collection. The documents should be in plain text format, with one document per file. Place all the document files in a directory.

4. Update the `buildIndex` constant in the `InvertedIndex003.java` file with the path to your document directory.

5. Build the project.

6. Run the `BooleanSearch` function.

7. Enter your boolean search query when prompted. The algorithm will process the query and return the relevant documents that match the query.

## Example

Suppose you have a directory called `documents` containing the following files:

- `doc1.txt`: "The quick brown fox jumps over the lazy dog."
- `doc2.txt`: "The cat and the dog are good friends."
- `doc3.txt`: "The fox is known for its cleverness."

You want to search for documents that contain the words "fox" and "dog". Follow the steps mentioned in the Usage section, and when prompted for the query, enter:

```
fox AND dog
```

The algorithm will process the query and return the following results:

```
Matching documents for the query 'fox AND dog':
- doc1.txt
- doc2.txt
```

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.
