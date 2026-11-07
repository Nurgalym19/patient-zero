# Assignment 4. Patient Zero

Student ID:254965
Name:Nurgalym Ermek
Group:SE-2519

This file is the report. Fill it in as you go. How to start: `START_HERE.md`. The task: `docs/Assignment4.pdf`.

## Program output

The 9 lines your program prints on your data (`java -cp out Main`):

    Mutations:93
    Shared core:994
    Marker hits:7
    First hit:6063
    Border length:3
    Shared words:378
    Tests needed:39
    Cheapest testing:188
    Longest chain:28

My outbreak report (a screenshot of `output/outbreak.html`):

![outbreak](output/outbreak.png)

## Questions

Answer in your own words, two or three sentences each.

Mission 1. What does the cell d[i][j] of your table mean, and how is it computed from other cells?
The cell d[i][j] is the minimum number of edits (replace, insert, delete) needed to turn the first i letters of strain A into the first j letters of strain B. If A[i-1] and B[j-1] are equal, the cell is just d[i-1][j-1]. Otherwise it is 1 plus the smallest of three neighbours: the diagonal cell (replace a letter), the cell above (delete a letter from A) and the cell on the left (insert a letter). The answer is the last cell d[n][m].

Mission 2. How is the longest common subsequence different from the longest common substring? My Shared core is bigger than the number of letters that are the same at the same place: why?
In a common subsequence the letters must keep their order but can have gaps between them, while in a common substring the letters must stand next to each other without gaps. My Shared core (994) is bigger than the number of positions with the same letter because after an insertion or a deletion the letters of the strains shift, so equal letters are no longer at the same place, but LCS can still match them in the right order across the gaps.

Mission 3. What does the prefix function tell you, and why does the pointer in the text never go back in KMP? What is the Border length of my marker and what does it mean?
The prefix function pi[i] is the length of the longest proper prefix of the marker’s first i+1 letters that is also its suffix. In KMP, when a letter does not match, only the pointer in the marker moves back (k = pi[k-1]), because the letters that already matched are known from the marker itself. The pointer in the text only goes forward, so the search takes O(n+m). The Border length of my marker is 3: its last 3 letters are equal to its first 3 letters, so after a full match the next occurrence can overlap with the previous one by 3 letters.

Mission 4. Why must you compare the letters after the hashes are equal? What happens to the cost of Rabin-Karp if many windows have the same hash?
Two different windows can have the same hash (a collision), so equal hashes do not prove that the words are equal. Without comparing the letters I would count words that are not really in the other strain and the answer would be too big. If many windows have the same hash, every hash match costs up to k letter comparisons, so the time grows from about O(n+m) towards O(n*k) in the worst case.

Mission 5. What are the two states of a person in your DP, and how do you get the answer for a person from its children? Why does a greedy "test the person with most links first" not always give the cheapest answer?
Every person has two states: in[v] when the person is tested and out[v] when the person is not tested. in[v] = cost[v] + the sum over children c of min(in[c], out[c]), because a tested person already covers the links to all children. out[v] = the sum of in[c] over children, because if v is not tested, every child must be tested to cover its link with v. The answer is min(in[0], out[0]) for the root. The greedy “most links first” does not always work because it ignores the costs and the shape of the tree: a person with many links can be very expensive, while testing a few cheap neighbours covers the same links for less money.

## AI use

I used Claude  as a helper while doing this assignment. I used it for the following things:

To understand the task: I asked Claude to explain the assignment step by step, what each mission is about, and the ideas behind edit distance, longest common subsequence, the prefix function and KMP, Rabin-Karp with a rolling hash, and dynamic programming on a tree.
To fix technical problems: Claude helped me with running Java in IntelliJ and in the terminal (the “java is not recognized” error, the working directory error in Check.java and the need to recompile before running).To help with the README: Claude helped me understand the questions and the timing and complexity tables. I checked the answers and wrote the final text myself.
## Timing (Mission 7)

Milliseconds, average of 3 runs, from `java -cp out Bench`.

| test | size | time |
|---|---|---|
| naive search | 20 000 / 40 000 / 80 000 |116,5 / 188,9 / 391,6 ms |
| KMP | 20 000 / 40 000 / 80 000 |4,9 / 2,6 / 1,4 ms |
| edit distance | 500 / 1000 / 2000 / 4000 |12,4 / 11,7 / 42,7 / 171,0 ms |

What do the numbers say about how the time grows? When you double the size, by how much does each time grow? Two or three sentences.

When the text doubles, the naive search time grows about 2 times (116.5 → 188.9 → 391.6 ms), but it is much slower than KMP because at every position it compares almost the whole pattern of 2000 letters (O(n·m)). KMP stays at a few milliseconds and does not grow with the text (4.9, 2.6, 1.4 ms); the small differences are only noise of the JVM warm-up, because KMP is O(n+m) and its pointer in the text never goes back. Edit distance grows about 4 times when the length doubles (42.7 → 171.0 ms from 2000 to 4000), because the table has n·m cells; the first two values (12.4 and 11.7 ms) are almost equal because of the warm-up on small sizes.
## Complexity

n is the length of the text (or of a strain), m the length of the pattern, k the length of the window, v the number of people. Fill in every algorithm you wrote.

| algorithm | time | space | assumptions |
|---|---|-------|---|
| Edit distance |O(n·m) | O(n·m) |full table (n+1)×(m+1), O(1) per cell |
| LCS |O(n·m) | O(n·m) |same table size as edit distance |
| Prefix function and KMP |O(m) + O(n) | O(m)	      |text pointer never goes back, returns by pi are amortized O(1) |
| Rabin-Karp (shared words) |O(n+m) on average, O(n·k) worst case | O(m)      |window hashes of B in a HashMap, worst case when many hashes collide |
| Tree DP |O(v) | O(v)      |every person visited once, recursion depth up to v |

## Before you submit

- The four files in `data` and the screenshot `output/outbreak.png` are in the repository.
- `java tools/Check.java` shows all examples right.
- This README is filled in, the program output and the section AI use included.
- The work is pushed to main (at least one commit per mission) and the repository is public.
- The link is attached in Moodle.
