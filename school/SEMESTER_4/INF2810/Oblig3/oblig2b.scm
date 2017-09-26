;;;; johntj, gudmunj, tormobr
;;;; Innlevering 2b

(#%require racket/trace)
(load "precode.scm")

;; Oppgave 1a:
"Oppgave 1a:"

(define (make-counter)
  (let ((count 0))
    (lambda ()
      (set! count(+ count 1))
      count)))

(define count 42)
(define c1 (make-counter))
(define c2 (make-counter))
(c1)
(c1)
(c1)
count
(c2)

;; Oppgave 1b:
;;                                                          p: <none>
;;                                                          b: (let...)
;;                                                          ^
;; +-------------------------------------------+            |
;; | GLOBAL ENVIRONMENT                        |           (o) (o)
;; |                                           |              | |
;; | count: 42                  make-counter:--+--------------+ |
;; |                                           |<---------------+
;; |                                           |
;; |                                           | <-------+
;; | c1:                                   c2: |         |
;; +------+---------------------------------+--+         |
;;        |            ^                    |            |
;;        |            |                    |            |
;;        |            |                    |            |
;;        |            |                    |            |
;;        |     +------+------+             |     +------+------+
;;        |     |E1           |             |     |E3           |
;;        |     +------+------+             |     +------+------+
;;        |            ^                    |            ^
;;        |            |                    |            |
;;        |            |                    |            |         
;;        |     +------+------+             |     +------+------+ 
;;        |     |E2. count: 3 |             |     |E4. count: 0 | <--+
;;        |     +------+------+             |     +------+------+    |
;;        |            |                    |            |           |
;;        |            |                    |            |           |
;;        | +----------+                    | +----------+           |
;;        | |                               | |                      |
;;     (o) (o)                           (o) (o)                +----+----+        
;;      |                                 |                     |E5       | 
;;      V                                 |                     +----+----+
;;      p: <none>       <-----------------+                      (set! ...)
;;      b (set! ...)

;;Oppgave 2a:
"Oppgave 2a:"

(define (make-stack-help new stack)
  (if (null? new)
      stack
      (make-stack-help (cdr new) (cons (car new) stack))))

(define (make-stack list)
  (let ((stack list))
    (lambda (message . rest)
      (cond((equal? message 'pop!)  
            (if(null? stack)
               "Stacken er tom"
               (set! stack(cdr stack))))
           ((equal? message 'stack) stack)
           ((equal? message 'push!) (set! stack (make-stack-help rest stack)))
           (else "Ugyldig input")))))



(define s1 (make-stack (list 'foo 'bar)))
(define s2 (make-stack '()))
(s1 'pop!)
(s1 'stack)
(s2 'pop!)
(s2 'push! 1 2 3 4)
(s2 'stack) 
(s1 'push! 'bah)
(s1 'push! 'zap 'zip 'baz)
(s1 'stack)
(s1 'stadfck)

;; Oppgave 2b:
"Oppgave 2b:"

(define (stack in)
  (in 'stack))

(define (pop! in)
  (in 'pop!))

(define (push! in . rest)
  (apply in (cons 'push! rest)))

(pop! s1)
(stack s1)
(push! s1 'foo 'faa)
(stack s1)


;; Oppgave 3a og b:
;;
;;
;; Før kall på set-cdr!
;;
;;          +-------+-------+        +-------+-------+        +-------+-------+        +-------+-------+        +-------+-------+              
;;          |       |       |        |       |       |        |       |       |        |       |       |        |       |    /  |
;; bar ---> |   o   |   o---+------> |   o   |   o---+------> |   o   |   O---+------> |   o   |   o---+------> |   o   |   /   |
;;          |   |   |       |        |   |   |       |        |   |   |       |        |   |   |       |        |   |   |  /    |
;;          +---+---+-------+        +---+---+-------+        +---+---+-------+        +---+---+-------+        +---+---+-------+
;;              |                        |                        |                        |                        |
;;              |                        |                        |                        |                        |
;;              |                        |                        |                        |                        |
;;              v                        v                        v                        v                        v
;;              a                        b                        c                        d                        e
;;
;;
;; 
;;
;;  Etter kall på set-cdr!               +---------------------------------------------------------+
;;                                       |                                                         |
;;                                       V                                                         |
;;          +-------+-------+        +-------+-------+        +-------+-------+        +-------+---+---+           
;;          |       |       |        |       |       |        |       |       |        |       |   |   | 
;; bar ---> |   o   |   o---+------> |   o   |   o---+------> |   o   |   O---+------> |   o   |   O   |
;;          |   |   |       |        |   |   |       |        |   |   |       |        |   |   |       | 
;;          +---+---+-------+        +---+---+-------+        +---+---+-------+        +---+---+-------+   
;;              |                        |                        |                        |                 
;;              |                        |                        |                        |                 
;;              |                        |                        |                        |       
;;              v                        v                        v                        v
;;              a                        b                        c                        d
;;
;;
;;  Før kall på set-car!
;;
;;          +-------+-------+        +-------+-------+        +-------+-------+             
;;          |       |       |        |       |       |        |       |    /  |
;; bah ---> |   o   |   o---+------> |   o   |   o---+------> |   o   |   /   |
;;          |   |   |       |        |   |   |       |        |   |   |  /    |
;;          +---+---+-------+        +---+---+-------+        +---+---+-------+
;;              |                        |                        |
;;              |                        |                        |
;;              |                        |                        |
;;              v                        v                        v
;;            bring                      a                      towel
;;
;;
;;
;;  Etter kall på set-car!
;;
;;        +-------+-------+        
;;        |       |       |        
;; bah -> |   o   |   o   |
;;        |   |   |   |   |        
;;        +---+---+---+---+        
;;            |       |
;;            |       |                                
;;             \     /                  
;;              v   v
;;            +-------+-------+        +-------+-------+ 
;;            |       |       |        |       |    /  | 
;;            |   o   |   o---+------> |   o   |   /   |
;;            |   |   |       |        |   |   |  /    | 
;;            +---+---+-------+        +---+---+-------+
;;                |                        |                  
;;                |                        |              
;;                |                        |     
;;                v                        v 
;;                a                      towel
;;
;;
;;
;; Når vi gjør det siste kallet på set-car! erstattes a med 42 "begge steder"
;; Hvis vi ser på tegningene våre ser vi hvorfor dette skjer.
;; begge de to første pekerene peker på det samme "objektet" i minnet.
;; når vi da endrer verdien, endres verdien begge stedene i listen vår.
;;
;;
;;
;; Oppgave 3c:"
;;
;; En liste i scheme er definert ved at den har en endelig lengde og at den
;; termineres av den tomme listen.
;; bar er en sirkulær liste hvor vi peker tilbake på b fra d, noe som gjør
;; at den ikke en endelig, og termineres ikke.
;; bah på den andre siden, er endelig og termineres av den tomme listen.
;; Av disse årsakene defineres derfor bah som en liste.


;; Oppgave 4a og b:
"Oppgave 4a og b:"

(define OG-procs (make-table)) ; Table som holder på originale prosedyrer.

(define (mem message proc)
  (let ((table (make-table))
        (original proc))
    (if (equal? message 'memoize)
        (let ((new-proc
               (lambda args
                 (let ((prev-res (lookup args table)))
                   (or prev-res
                       (let ((result (apply proc args)))
                         (insert! args result table)
                         result))))))
          (insert! new-proc original OG-procs)
          new-proc)
        (if(equal? message 'unmemoize)
           (lookup proc OG-procs)
           "Wrong input m8!"))))


(fib 3)
(set! fib (mem 'memoize fib))

(fib 3)

(fib 3)

(fib 2)

(fib 4)

(test-proc 40 41 42 43 44)

(set! test-proc (mem 'memoize test-proc))

(test-proc)

(test-proc)

(test-proc 40 41 42 43 44)

(test-proc 42 43 44)

(set! fib (mem 'unmemoize fib))

(fib 3)

;; Oppgave 4c:
"Oppgave 4c:"

;; Lite eksempel for å demonstrere forskjell på define og set!.
(define a 1)

(define (hax)
  (define a 2)
  a)

(define (hax2)
  (set! a 2)
  a)

(hax) ; returnerer 2
a     ; returnerer 1
(hax2); returnerer 2
a     ; returnerer 2



;; Som vi ser endres ikke verdien til a på utsiden når vi gjør kallet på hax,
;; som bruker (define a 2). Når vi gjør kallet på hax2, som bruker (set! a 2).
;; Med mem-fib vil vi kun memoize resultatet fra det opprinelige kallet, og ikke
;; de rekursive kallene som gjøres underveis. Dette fordi det ikke oppstår noen direkte binding
;; til prosedyren fib. Med set! derimot, bindes fib, direkte til den memoizerte versonen av fib.
;; Det fører til at også alle de rekursive kallene utføres med den memoizerte versonen.
;; Hvis vi kaller (mem-fib 3) to ganger vil den huske resultatet fra den første gangen,
;; men hvis vi kaller først (mem-fib 3) så (mem-fib 2) vil ikke resultatet til (mem-fib 2)
;; være lagret, pga. dette skjer som et rekursivt kall, hvor det ikke memoizes i (mem-fib 3).

