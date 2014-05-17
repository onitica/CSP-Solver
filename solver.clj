;Takes a map of the following fields:
;@constraints - A vector of constraints on the CSP. Currently only supports the alldiff string.
;@domains - A map of variables and their domain values.

;Checks whether a map has an empty value
(defn empty-val? [m]
	(some #(empty? (second %)) m))

;Removes from a map set all values
(defn remove-map-set-val [m v]
	 (zipmap (keys m) (map #(disj % v) (vals m))))
	
;Check whether a map with set values has a value in the sets
;Basic constraint checking - Do we have assignable values from domain
(defn has-val? [m v]
	(some #(some #{v} (second %)) m))

;Check if a value violates an alldiff constraint
(defn violates-alldiff? [domain-val solution]
	(has-val? solution domain-val))

;Need to add other type of constraints here
(defn violates-constraints? [domain-val constraints solution]
	(loop [constraints constraints violation nil]
		(let [c (first constraints)]
		(cond (not (nil? violation)) true
			  (nil? c) false
			  (= "alldiff" c) (recur (rest constraints) (violates-alldiff? domain-val solution))
			  :else (println "ERROR: violates-constraints? undefined behavior.")))))

;@param - obj is a map containing :domain and :constraints
;@return - a map containing the new :domain, :constraints without unary, and :partial-solution. 
;If return is nil, the problem is not node consistent
;Currently only handles all-diff optimization, does not handle real unary constraints
;Unary constraints should just be applied to the solution and then removed
(defn node-consistent [CSP]
	(let [constraints (:constraints CSP)]
		(if (some #{"alldiff"} constraints)
			(let [group-by-count (group-by #(> (count (second %)) 1) (:domains CSP)) ;Get 1 var domains
				  partial-solution (into {} (group-by-count false)) ;Set 1 var domains as solution
				  domains (apply dissoc (:domains CSP) (keys partial-solution))] ;Remove from old domains
				(if (not= (count (apply clojure.set/union (vals partial-solution))) (count partial-solution))
					nil
					{ :domains domains, :constraints constraints, :partial-solution partial-solution}))
			(merge CSP {:partial-solution {}}))))
			
;Currently does nothing
(defn arc-consistent [CSP]
	CSP)
	
;Remove the first value from a domain for backtracking purposes
(defn remove-domain-val [domains domain-ele]
	(assoc domains (first domain-ele) (rest (second domain-ele))))

;Currently only implements backtracking
;Prev is a vector, first element if the domain, second element is the partial solution
(defn solve-CSP	[CSP-raw]
	(let [CSP (node-consistent CSP-raw)
		  constraints (:constraints CSP)]
	(if (nil? CSP) "ERROR: Not node-consistent"
	(loop [prev [] domains (:domains CSP) solution (:partial-solution CSP)]
		(if (empty? domains) (if (= (count solution) (count (:domains CSP-raw))) solution "ERROR: Not satisfiable")
			(let [domain-ele (first domains)
				  domain-val (first (second domain-ele))
				  domain-key (first domain-ele)] 
				(cond (or (empty-val? domains) (violates-constraints? domain-val constraints solution))
					  (if (empty? prev) "ERROR: Not satisfiable"
									    (recur (rest prev) (first (first prev)) (second (first prev))))
					  :else  (recur (conj prev [(remove-domain-val domains domain-ele) solution]) 
					   		   		(remove-map-set-val (rest domains) domain-val) 
					   		   		(merge solution {domain-key #{domain-val}})))))))))