(ns clojure.core-test.star
  (:require [clojure.test :as t :refer [deftest testing is are]]
            [clojure.core-test.number-range :as r]))

(deftest common
  (are [prod x y] (= prod (* x y) (* y x))
    0 0 0
    0 0 1
    1 1 1
    5 1 5
    25 5 5
    -1 1 -1
    1 -1 -1
    0 0 -1
    (inc r/min-int) r/max-int -1

    0.0 0.0 0.0
    0.0 0.0 1.0
    1.0 1.0 1.0
    5.0 1.0 5.0
    25.0 5.0 5.0
    -1.0 1.0 -1.0
    1.0 -1.0 -1.0
    0.0 0.0 -1.0

    0.0 0.0 0
    0.0 1.0 0
    0.0 0.0 1
    1.0 1.0 1
    5.0 1.0 5
    5.0 5.0 1
    25.0 5.0 5
    -1.0 1.0 -1
    -1.0 -1.0 1
    1.0 -1.0 -1
    0.0 -1.0 0
    0.0 0.0 -1

    0.0 0 0.0
    0.0 1 0.0
    0.0 0 1.0
    1.0 1 1.0
    5.0 1 5.0
    5.0 5 1.0
    25.0 5 5.0
    -1.0 1 -1.0
    -1.0 -1 1.0
    1.0 -1 -1.0
    0.0 -1 0.0
    0.0 0 -1.0

    0 0 1N
    0 0N 1
    0 0N 1N
    1 1N 1
    1 1 1N
    1 1N 1N
    5 1 5N
    5 1N 5
    5 1N 5N)

  (is (thrown? Exception (* 1 nil)))
  (is (thrown? Exception (* nil 1)))

  #?@(:cljs nil
      :default
      [(is (instance? clojure.lang.BigInt (* 0 1N)))
       (is (instance? clojure.lang.BigInt (* 0N 1)))
       (is (instance? clojure.lang.BigInt (* 0N 1N)))
       (is (instance? clojure.lang.BigInt (* 1N 1)))
       (is (instance? clojure.lang.BigInt (* 1 1N)))
       (is (instance? clojure.lang.BigInt (* 1N 1N)))
       (is (instance? clojure.lang.BigInt (* 1 5N)))
       (is (instance? clojure.lang.BigInt (* 1N 5)))
       (is (instance? clojure.lang.BigInt (* 1N 5N)))

       (is (thrown? Exception (* -1 r/min-int)))
       (is (thrown? Exception (* r/min-int -1)))
       (is (thrown? Exception (* (long (/ r/min-int 2)) 3)))
       (is (thrown? Exception (* 3 (long (/ r/min-int 2)))))]))

(deftest rationals
  (are [prod x y] (= prod (* x y) (* y x))
    1     1/2  2/1
    1     1/2  2
    -1    1/2  -2
    -1    -1/2 2
    1     -1/2 -2
    1.0   1/2  2.0
    -1.0  1/2  -2.0
    -1.0  -1/2 2.0
    1.0   -1/2 -2.0
    1     1/2  2N
    -1    1/2  -2N
    -1    -1/2 2N
    1     -1/2 -2N
    1.0   1/3  3.0
    0     1/2  0
    0.0   1/2  0.0
    0     1/2  0N
    1/10  1/2  1/5
    -1/10 1/2  -1/5
    -1/10 -1/2 1/5
    1/10  -1/2 -1/5)

  (is (thrown? Exception (* 1/2 nil)))
  (is (thrown? Exception (* nil 1/2))))

(deftest inf-nan
  (testing "Multiplication with infinities"
    (are [prod x y] (= prod (* x y) (* y x))
      ##Inf  ##Inf  1
      ##Inf  ##Inf  1N
      ##Inf  ##Inf  1.0
      ##Inf  ##Inf  1/2
      ##Inf  ##Inf  2
      ##-Inf ##Inf  -1
      ##-Inf ##Inf  -1N
      ##-Inf ##Inf  -1.0
      ##-Inf ##Inf  -1/2
      ##-Inf ##-Inf 1
      ##-Inf ##-Inf 1N
      ##-Inf ##-Inf 1.0
      ##-Inf ##-Inf 1/2
      ##Inf  ##-Inf -1
      ##Inf  ##-Inf -1N
      ##Inf  ##-Inf -1.0
      ##Inf  ##-Inf -1/2
      ##Inf  ##Inf  2
      ##Inf  ##Inf  ##Inf
      ##-Inf ##Inf  ##-Inf
      ##-Inf ##-Inf ##Inf
      ##Inf  ##-Inf ##-Inf
      ##Inf  ##Inf  r/max-int
      ##-Inf ##Inf  r/min-int
      ##Inf  ##Inf  r/max-double
      ##Inf  ##Inf  r/min-double))

  (testing "Multiplication resulting in ##NaN"
    (are [x y] (and (NaN? (* x y))
                    (NaN? (* y x)))
      ##Inf 0                           ; Perhaps counter-intuitive
      ##Inf 0N
      ##Inf 0.0
      ##Inf 0/2
      ##NaN 1
      ##NaN 1N
      ##NaN 1.0
      ##NaN 1/2)))
