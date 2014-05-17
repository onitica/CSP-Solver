Beginning of a basic CSP solver in Clojure. Originally made for "http://www.reddit.com/r/dailyprogrammer/comments/24ypno/572014_challenge_161_medium_appointing_workers/", but is total overkill for it.

Currently supports:
-Backtracking
-Alldiff constraint

Future goals:
-Forward checking
-Unary constraints
-Binary constraints
-Finish node-consistency (i.e. handle other unary constraints)
-Finish arc-consistency

Distant future goals:
-Better structure (debugging/project layout/easy input/etc.)
-Speed optimizations
-Weighted constraints
-K-consistency (or breakdown)
-Domain separation and parallel execution