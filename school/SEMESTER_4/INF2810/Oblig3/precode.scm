;;;; Diverse hjelpekode for innlevering 3a i INF2810, 2017.
;;;; erikve [at] ifi.uio.no


;; Tabell-abstraksjon fra seksjon 3.3.3 i SICP:

(define (make-table)
  (list '*table*))

(define (lookup key table)
  (let ((record (assoc key (cdr table))))
    (and record (cdr record))))

(define (insert! key value table)
  (let ((record (assoc key (cdr table))))
    (if record
	(set-cdr! record value)
	(set-cdr! table 
		  (cons (cons key value) (cdr table))))))


;; mem-testprosedyre 1; fibonacci-tallene
(define (fib n)
  (display "computing fib of ")
  (display n) (newline)
  (cond ((= n 0) 0)
        ((= n 1) 1)
        (else (+ (fib (- n 1))
                 (fib (- n 2))))))


;; mem-testprosedyre 2; tar virkårlig mange argumenter (null eller flere).
;; (Returnerer summen av argumentenes kvadrerte forskjell fra 42.)
(define (test-proc . args)
  (display "computing test-proc of ")
  (display args) (newline)
  (if (null? args)
      0
      (+ (expt (- 42 (car args)) 2)
	 (apply test-proc (cdr args)))))
