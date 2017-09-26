(#%require racket/trace)
;; oppgave 1f:
;; Lager variabler for bar og selve listen, for å gjøre det mer ryddig. Gjelder de 2 neste oppgavene også.
"oppgave 1f:"

(define bar 200)

(define foo (list 0 42 #t bar))

(car(cdr foo))


;;oppgave 1g:
"oppgave 1g:"

(define foo2 (list '(0 42) '(#t bar)))

(car(cdr(car foo2)))


;;oppgave 1h:
"oppgave 1h:"

(define foo3(list '(0) '(42 #t) '(bar)))

(car(car(cdr foo3)))


;;oppgave 1i - cons:
"oppgave 1i:" 
;;delte det opp slik at det blir litt mer leselig.

(define a(cons 0(cons 42 '())))

(define b(cons #t(cons bar '())))

(cons a(cons b '()))


;;oppgave 1i - list:
;;også delt opp, slik at det er mer tydelig.

(define a2(list 0 42))

(define b2(list #t bar))

(list a b)


;;oppgave 2a:
"oppgave 2a:"

(define (length2 items)
  (define (counter items count)
    (if (null? items)
        count
        (counter (cdr items) (+ count 1))))
  (counter items 0))
 

(length2 '(1 2 3 5 4))

;;oppgave 2b:
;;Dette er en hale rekursiv funksjon som gir opphav til en iterativ prosess.
"oppgave 2b:"

(define (reduce-reverse proc init items)
  (if (null? items)
      init
      (reduce-reverse proc (proc (car items) init)(cdr items))))



(reduce-reverse cons '() '(1 2 3 4 5))
(reduce-reverse * 1 '(1 2 3 4 5))
(reduce-reverse + 0 '(1 2 3))


;;oppgave 2c:
"oppgave 2c:"

(define (all? pred items)
  (if (null? items)
      #t
      (if (pred (car items))
          (all? pred (cdr items))
          #f)))

(all? even? '(1 3 5 6 7 9))
(all? odd? '(1 3 5 7 9))
(all? odd? '())

(all? (lambda(x)
        (and (< x 10)))
      '(1 2 3 4 50))


;;oppgave 2d:
"oppgave2d:"

(define (nth i items)
  (if (zero? i)
      (car items)
      (nth (- i 1) (cdr items))))


(nth 2 '(47 11 12 13))
(nth 4 '(12 1 7 8 41 53 61))


;;oppgave 2e:
"oppgave 2e:"

(define (where num items)
  (define (counter count items)
    (if (null? items)
        #f
        (if (= (car items) num)
            count
            (counter (+ count 1) (cdr items)))))
  (counter 0 items))


(where 3 '(1 2 3 4 5))


;;oppgave 2f:
"oppgave 2f:"


(define (map2 proc items1 items2)
  (cond ((null? items1) '())
        ((null? items2) '())
        (else(cons(proc (car items1) (car items2))
                  (map2 proc (cdr items1) (cdr items2))))))


(map2 + '(1 2 3 4) '(3 4 5))
(map2 * '(4 2) '(8 2 3 4))
(map2 - '() '(1 2 3 4))


;;oppgave 2g:
"oppgave 2g:"

(map2 (lambda(x y)
        (/ (+ x y) 2))
      '(1 2 3 4) '(3 4 5))

(map2 (lambda(x y)
        (/ (+ x y) 2))
      '(1 3 5 7) '(4 6 9))


;;oppgave 2h:
"oppgave 2h:"

(define (both? pred)
  (lambda(x y)
    (if (and (pred x) (pred y))
        #t
        #f)))


(map2 (both? even?) '(1 2 3) '(3 4 5)) 
((both? even?) 2 4)
((both? even?) 2 5)


;;oppgave 2i:
"oppgave 2i:"

(define (self proc)
  (lambda(x)
    (proc x x)))

((self +) 5)
((self *) 3)
(self +)
((self list) "hello") 