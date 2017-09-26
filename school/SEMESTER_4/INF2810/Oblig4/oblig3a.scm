;; Oblig 3a INF-2810
;; Kangeyan Illavalagan	kangeyai
;; Tormod Brændshøi tormobr


(load "prekode3a.scm")

;; tullekode
;;
;;
(define (interval low high)
  (if (> low high)
      '()
      (cons low (interval (+ low 1) high))))


(interval 1 5)


(define (filter pred list)
  (cond ((null? list) '())
        ((pred (car list)) (cons (car list) (filter pred (cdr list))))
        (else (filter pred (cdr list)))))


(define (prime? num)
  (define (rec count)
    (cond((< num 2) #f)
         ((= count 1) #t)
         ((= 0 (modulo num count)) #f)
         (else (rec (- count 1)))))
  (rec (- num 1)))


;;
;;
;;


;;oppgave 1a:
(newline)
"Oppgave 1a:"
(define (list-to-stream list)
  (if (null? list)
      the-empty-stream
      (cons-stream (car list)(list-to-stream (cdr list)))))

(define (stream-to-list stream . n)
  (cond ((null? n)
         (if (stream-null? stream) the-empty-stream
             (cons (stream-car stream) (stream-to-list (stream-cdr stream)))))
        (else
         (if (or (stream-null? stream)(= (car n) 0)) the-empty-stream
             (cons (stream-car stream) (stream-to-list (stream-cdr stream) (- (car n) 1)))))))


         

                        
(list-to-stream '(1 2 3 4 5 5 6))

(stream-to-list (stream-interval 10 20))

(show-stream nats 15)

(stream-to-list nats 10)


;;oppgave 1b:
(newline)
"Oppgave 1b:"

(define (check-empty . streams)
  (if (equal? '(()) streams)
      #f
      (if (stream-null? (caar streams))
          #t
          (check-empty (cdar streams)))))

(define (stream-map proc . argstreams)
  (if (check-empty argstreams)
      the-empty-stream
      (cons-stream
       (apply proc (map stream-car argstreams))
       (apply stream-map
              (cons proc (map stream-cdr argstreams))))))


(define stream1 (list-to-stream '(2 4 6 8)))
(define stream2 (list-to-stream '(1 3 5 7 9)))
stream1
stream2
(show-stream stream1)
(stream-map * (stream-interval 10 20) (stream-interval 10 20))
(stream-map * stream1 stream2)


;;Oppgave 1c:
(newline)
"Oppgave 1c:"


(define (memq2 item x)
  (cond ((stream-null? x) #f)
        ((eq? item (stream-car x)) x)
        (else (memq2 item (stream-cdr x)))))


(define (remove-duplicates2 lst)
  (cond ((stream-null? lst) the-empty-stream)
        ((not (memq2 (stream-car lst) (stream-cdr lst)))
         (cons-stream (stream-car lst) (remove-duplicates2 (stream-cdr lst))))
        (else (remove-duplicates2 (stream-cdr lst)))))


(define stream3 (list-to-stream '(1 3 5 1 9 5 8 2 6 7 3 8 9 1 4 5 6 9 1 4 6 8 0 4 4 6 2 8 7)))

(remove-duplicates2 stream3)

(show-stream (remove-duplicates2 stream3))

;; Et problem med petter smart sin løsning kan være at en strøm kan være uendelig lang.
;; Hvis vi f.eks. bruker nats som er strømmen av alle tall fra 1, vil den aldri stoppe å lete etter duplikater.
;; Dette vil føre til at programmet står og kjører uendelig lenge. Som vi ser fungerer det fint med en endelig strøm.;;
;;Kunne eventult søkt bakover i stedet for forover


;;Oppgave 1d:
(newline)
"Oppgave 1d:"
(define (hax x)
  (display x)
  (newline))

(define x
  (stream-map show
              (stream-interval 0 10)))

(stream-ref x 5)

(stream-ref x 7)

;; Når vi kaller på (stream-ref x 5) vil den delaye på 5, og når vi da kaller på (stream-ref x 7) vil den force videre slik at 6 og 7 skrives ut.
;; Den vil med andre ord ikke skrive ut 1, 2, 3, 4 og 5 siden de har allerede blitt forcet ut når vi gjorde det forrige kallet.


;;Oppgave 2a:
"Oppgave 2a:"

;; Lager en tom table
(define (make-lm)
  (list '*table*))

;;Slår opp ett ord-par i tabellen
(define (lm-lookup-bigram lm string-1 string-2)
  (let ((subtable (assoc string-1 (cdr lm))))
    (if subtable
        (let ((record (assoc string-2 (cdr subtable))))
          (if record
              (cdr record)
              #f))
        #f)))

;;Legger inn en ny forekomst av et ordpar i tabellen.
(define (lm-record-bigram! lm string-1 string-2)
  (let ((subtable (assoc string-1 (cdr  lm))))
    (if subtable
        (let ((record (assoc string-2 (cdr subtable))))
          (if record
              (set-cdr! record (+ (cdr record) 1))
              (set-cdr! subtable
                        (cons (cons string-2 1)
                              (cdr subtable)))))
        (set-cdr! lm
                  (cons (list string-1
                              (cons string-2 1))
                        (cdr lm)))))
  'recorded)

;;Div tester, for å se at alt fungerer slik det skal
(define test1 (make-lm))
(lm-record-bigram! test1 "the" "this")
(lm-record-bigram! test1 "the" "this")
(lm-record-bigram! test1 "the" "this")
(lm-lookup-bigram test1 "the" "this") ; 3


;;; 2b)
(newline) '2b
;; init
(define brown (read-corpus "brown.txt")) 
(define brown-lm (make-lm))

;;Leser inn en liste, og legger det inn i språkmodellen vår, med frekvenser.
(define (lm-train! lm list)
  (define (iter list lm)
    (if (null? list)
        'complete
        (begin
          (sentence (car list) lm)
          (iter (cdr list) lm))))
  (define (sentence list lm)
    (if (null? (cdr list))
        'done
        (begin
          (lm-record-bigram! lm (car list) (car (cdr list)))
          (sentence (cdr list) lm))))
  (iter list lm))

;; Tester at alt fungerer.
(lm-train! brown-lm brown)
(lm-lookup-bigram brown-lm "jury" "said")


;;; 2c)
(newline) '2c

;; Teller antall forekomster til en nøkkel-string
(define (count list)
  (define (iter sum list)
    (if (null? list)
        sum
        (iter (+ sum (cdr (car list))) (cdr list))))
  (iter 0 list))

;; bruker antall forekomster til en nøkkel-string fra "count" og regner ut sannsynelighet.
(define (calculate sub-lm)
  (let ((sum (count sub-lm)))
    (define (iter sub-lm)
      (if (null? sub-lm)
          'done
          (begin
            (set-cdr! (car sub-lm) (* (cdr (car sub-lm)) (/ 1 sum)))
            (iter (cdr sub-lm)))))
    (iter sub-lm)))

;; Estimerer sannsynelighet for alt i tabellen, ved hjelp av de to prosedyrene over.
;; Brukte en litt juksemetode for å finne det totale antall forekomster
;; som skal brukes senere i oppgave 2d.
;; Erstatter (car lm) fra å være '*table* til å lagre total antall forekomster.
(define (lm-estimate! lm-in)
  (find-total lm-in)
  (define (iterate lm)
    (if (null? lm)
        'done
        (begin
          (calculate (cdr (car lm)))
          (iterate (cdr lm)))))
  (iterate (cdr lm-in)))


;;Div tester
(define test (read-corpus "test.txt"))
(define test-lm (make-lm))
(lm-train! test-lm test)




;;Oppgave 2d:
"Oppgave 2d:"

;; prosedyre som regner ut total antall forekomster.
(define (find-total lm)
  (define (iter lm sum)
    (if (null? lm)
        sum
        (iter (cdr lm) (+ sum (count(cdr (car lm)))))))
  (set-car! lm (iter (cdr lm) 0)))

(lm-estimate! brown-lm)

;; Regner ut sannsyneligheten for en setning, ved hjelp av formel gitt i oppgaveteksten.
;; Forutsetter at man har gjort et kall på "lm-estimate", slik at totale antall forekomster
;; blir lagret.
(define (lm-score lm string)
  (let ((total (car lm)))
    (define (iter lm string sum)
      (if (null? (cdr string))
          sum
          (let ((new (lm-lookup-bigram lm (car string) (car(cdr string)))))
            (if (not new)
                (iter lm (cdr string) (* sum (/ 1 total)))
                (iter lm (cdr string)(* sum new))))))
    (iter lm string 1)))


(lm-score brown-lm '("<s>" "That's" "impossible." "</s>"))







