(load "loader")
(load "solver")

(defn pretty-print-solution [solution]
	(doseq [element (into [] (sort solution))]
		(println (name (first element)) (first (second element))))
	(println "*************"))

(defn impossible-tests []
	(doseq [data (map load-reddit-CSP-file ["fail1.txt", "fail2.txt"])]
		(println (solve-CSP data))))

(defn reddit-dp-161-tests []
	(doseq [data (map load-reddit-CSP-file ["data1.txt", "data2.txt", "data3.txt", "data4.txt"])]
		(time (solve-CSP data))
		(pretty-print-solution (solve-CSP data))))
		
(println "These tests should fail (impossible to solve under constraints)")
(println "*************")
(impossible-tests)
(println "Running tests:")
(println "*************")
(reddit-dp-161-tests)
	
	