;Load data and transform it into a domain, variables, and constraints

(defn does-not-contain-char [s c]
	(not (some #(= c %) s)))

(defn parse-worker [worker]
	(let [[name jobs] (clojure.string/split worker #"\s")]
		{ (keyword name) (into #{} (clojure.string/split jobs #"\,")) }))

(defn parse-input [filename]
	(let [data (clojure.string/split (slurp filename) #"\n")
		  num-jobs (first data)
		  [jobs raw-workers] (split-with #(does-not-contain-char % \,) (next data))
		  workers (reduce #(merge %1 (parse-worker %2)) {} raw-workers)]
		{ :n num-jobs, :jobs jobs, :workers workers }))
		
(defn convert-to-CSP [input-obj]
	{ :constraints ["alldiff"], :domains (:workers input-obj)})

;Problem is split so the variables are the workers, the constraint is alldiff, and the domain is the possible 
;assignments for each worker

(defn load-reddit-CSP-file [filename]
	(convert-to-CSP (parse-input filename)))